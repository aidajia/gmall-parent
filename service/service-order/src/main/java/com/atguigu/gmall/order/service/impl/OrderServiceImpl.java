package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.common.constants.MqConst;
import com.atguigu.gmall.common.constants.RedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.AuthUtil;
import com.atguigu.gmall.common.util.JSONS;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.feign.pay.PayFeignClient;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.feign.user.UserFeignClient;
import com.atguigu.gmall.feign.ware.WareFeignClient;
import com.atguigu.gmall.model.cart.CartItem;
import com.atguigu.gmall.model.enums.OrderStatus;
import com.atguigu.gmall.model.enums.PaymentWay;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.mqto.order.OrderCreateTo;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.to.UserAuthTo;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.model.vo.order.CartItemForOrderVo;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.atguigu.gmall.order.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartFeignClient cartFeignClient;

    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    ProductFeignClient productFeignClient;

    @Autowired
    WareFeignClient wareFeignClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    ThreadPoolExecutor corePool; //@Primary

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    PayFeignClient payFeignClient;


    @Override
    public OrderConfirmVo getOrderConfirmData() {
        OrderConfirmVo confirmVo = new OrderConfirmVo();

        Result<List<CartItem>> checkItem = cartFeignClient.getCheckItem();
        if (checkItem.isOk()) {
            List<CartItem> items = checkItem.getData();

            List<CartItemForOrderVo> vos = items.stream()
                    .map(cartItem -> {
                        CartItemForOrderVo vo = new CartItemForOrderVo();
                        vo.setImgUrl(cartItem.getSkuDefaultImg());
                        vo.setSkuName(cartItem.getSkuName());
                        //再查实时价格
                        Result<BigDecimal> price = productFeignClient.getSkuPrice(cartItem.getSkuId());
                        vo.setOrderPrice(price.getData());
                        vo.setSkuNum(cartItem.getSkuNum());

                        //再实时查询下商品有货无货
                        String stock = wareFeignClient.hasStock(cartItem.getSkuId(), cartItem.getSkuNum());
                        vo.setStock(stock);
                        return vo;

                    }).collect(Collectors.toList());
            //设置所有选中的商品
            confirmVo.setDetailArrayList(vos);

            //计算总量
            Integer integer = items.stream().map(CartItem::getSkuNum)
                    .reduce((a, b) -> a + b)
                    .get();
            confirmVo.setTotalNum(integer);


            //计算价格
            BigDecimal bigDecimal = vos.stream().map(i -> i.getOrderPrice().multiply(new BigDecimal(i.getSkuNum().toString())))
                    .reduce((a, b) -> a.add(b))
                    .get();
            confirmVo.setTotalAmount(bigDecimal);

        }
        //设置用户地址列表
        Result<List<UserAddress>> userAddressList = userFeignClient.getUserAddressList();
        confirmVo.setUserAddressList(userAddressList.getData());

        //设置 tradeNo: 防重令牌,给redis一个
        String tradeNo = generateTradeNo();
        //防重令牌, 给页面一个
        confirmVo.setTradeNo(tradeNo);

        return confirmVo;
    }

    @Override
    public String generateTradeNo() {
        //1.生成防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        //保存到redis
        redisTemplate.opsForValue().set(RedisConst.NO_REPEAT_TOKEN+token,"1",10, TimeUnit.MINUTES);

        return token;
    }

    @Override
    public boolean checkTradeNo(String token) {
        //1. 原子验令牌+删令牌
        String script = "if redis.call('get', KEYS[1]) == '1' then return redis.call('del', KEYS[1]) else return 0 end";
        //2. 执行
        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(RedisConst.NO_REPEAT_TOKEN + token), "1");

        return result == 1L;
    }

    @Override
    public Long submitOrder(String tradeNo, OrderSubmitVo orderSubmitVo) {
        //1. 验令牌
        boolean no = checkTradeNo(tradeNo);
        if(!no){
            throw new GmallException(ResultCodeEnum.REQ_ILLEGAL_TOKEN_ERROR);
        }

        //2. 验价格: 验总价
        //2.1 前端提交来的所有商品的总价
        BigDecimal frontTotal = orderSubmitVo.getOrderDetailList().stream()
                .map(item -> item.getOrderPrice().multiply(new BigDecimal(item.getSkuNum().toString())))
                .reduce((a, b) -> a.add(b))
                .get();

        //2.2 购物车中这个选中商品的总价
        Result<List<CartItem>> checkItems = cartFeignClient.getCheckItem();
        BigDecimal backTotal = checkItems.getData().stream()
                .map(item -> {
                    Result<BigDecimal> skuPrice = productFeignClient.getSkuPrice(item.getSkuId());
                    BigDecimal price = skuPrice.getData();
                    Integer skuNum = item.getSkuNum();
                    return price.multiply(new BigDecimal(skuNum.toString()));
                })
                .reduce((a, b) -> a.add(b))
                .get();
        //2.3 对比 -1,0,or1
        if(backTotal.compareTo(frontTotal) != 0){
            throw new GmallException(ResultCodeEnum.ORDER_PRICE_CHANGE);
        }

        //3. 验库存, 提示精确
        List<String> noStock = new ArrayList<>();
        checkItems.getData().stream()
                .forEach(item -> {
                    String stock = wareFeignClient.hasStock(item.getSkuId(), item.getSkuNum());
                    if(!"1".equals(stock)){
                        //没有库存了
                        noStock.add("["+item.getSkuName()+": 没有库存]");
                    }
                });
        if(noStock.size() > 0){
            String msg = noStock.stream()
                    .reduce((a, b) -> a + ", " + b)
                    .get();
            GmallException exception = new GmallException(msg, ResultCodeEnum.PRODUCT_NO_STOCK.getCode());
            throw exception;
        }

        //4、保存订单
        Long orderId = saveOrder(orderSubmitVo);

        //获取老请求
        RequestAttributes oldReq = RequestContextHolder.getRequestAttributes();

        //5. 删除购物车中选中的商品
        corePool.submit(()->{
            //再给当前线程一放
            RequestContextHolder.setRequestAttributes(oldReq);
            log.info("正准备删除购物车选中的商品");
                cartFeignClient.deleteCartChecked();
        });

        //6、30min以后关闭这个订单
//        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
//
//        scheduledThreadPool.schedule(()->{
//            closeOrder(orderInfo);
//        },30,TimeUnit.MINUTES);

        //7、给 MQ 发送一个消息：表示某个订单创建成功了； orderId，userId。
        // 把他放到保存订单的事务环节了。
        //缺点：
        //  1)、MQ稳定性差会导致经常下单失败。


        return orderId;
    }

    /**
     * 保存订单信息
     * @param orderSubmitVo
     */
    @Transactional
    @Override
    public Long saveOrder(OrderSubmitVo orderSubmitVo) {
        //1. 将vo带来的数据, 转成订单保存数据库中的数据模型
        //order_info_1: 订单信息表. order_detail: 订单详情表.  order_detail是order_info的绑定
        OrderInfo orderInfo = prepareOrderInfo(orderSubmitVo);
        orderInfoService.save(orderInfo);


        //2.保存 order_detail
        List<OrderDetail> orderDetails = prepareOrderDetail(orderInfo);
        orderDetailService.saveBatch(orderDetails);

        //订单只要存到数据库就发消息。
        sendOrderCreateMsg(orderInfo.getId());

        return orderInfo.getId();

    }

    /**
     * 订单创建完成后,发送消息
     * @param orderId
     */
    @Override
    public void sendOrderCreateMsg(Long orderId) {
        Long userId = AuthUtil.getUserAuth().getUserId();

        OrderCreateTo orderCreateTo = new OrderCreateTo(orderId, userId);
        String json = JSONS.toStr(orderCreateTo);

        rabbitTemplate.convertAndSend(MqConst.ORDER_EVENT_EXCHANGE,
                MqConst.RK_ORDER_CREATE,json);

    }

    /**
     * orderInfo
     * @param orderId
     * @return
     */
    @Override
    public OrderInfo getOrderInfoIdAndAmount(Long orderId) {
        Long userId = AuthUtil.getUserAuth().getUserId();

        LambdaQueryWrapper<OrderInfo> wrapper =
                Wrappers.lambdaQuery(OrderInfo.class)
                .eq(OrderInfo::getId, orderId)
                .eq(OrderInfo::getUserId, userId);

        OrderInfo one = orderInfoService.getOne(wrapper);
        return one;
    }

    @Override
    public void updateOrderStatusToPAID(String outTradeNo) {
        //1. 查订单
        long userId = Long.parseLong(outTradeNo.split("-")[2]);

        //2. 修改
        ProcessStatus paid = ProcessStatus.PAID;
        orderInfoService.updateOrderStatusToPaid(outTradeNo,userId,paid.name(),paid.getOrderStatus().name());
    }

    @Override
    public void checkAndSyncOrderStatus(String outTradeNo) {
        //1.数据库查出此单
        long userId = Long.parseLong(outTradeNo.split("-")[2]);
        LambdaQueryWrapper<OrderInfo> wrapper = Wrappers.lambdaQuery(OrderInfo.class)
                .eq(OrderInfo::getUserId, userId)
                .eq(OrderInfo::getOutTradeNo, outTradeNo);
        OrderInfo orderInfo = orderInfoService.getOne(wrapper);

        //2. 支付宝查出此单
        Result<String> result = payFeignClient.queryTrade(outTradeNo);
        /**
         * TRADE_FINISHED
         * TRADE_SUCCESS  支付成功
         * WAIT_BUYER_PAY
         * TRADE_CLOSED
         */
        if("TRADE_SUCCESS".equals(result.getData()) && (orderInfo.getOrderStatus().equals(OrderStatus.UNPAID.name()) || orderInfo.getOrderStatus().equals(OrderStatus.CLOSED.name()))){
            //改成已支付即可
            updateOrderStatusToPAID(outTradeNo);
        }
    }

    /**
     * 准备订单单项数据
     * @param orderInfo
     * @return
     */
    private List<OrderDetail> prepareOrderDetail(OrderInfo orderInfo) {
        //1.拿到订单需要购买的所有商品
        List<CartItem> items = cartFeignClient.getCheckItem().getData();

        //2. 每个要购买的商品其实就是一个订单项数据
        List<OrderDetail> detailList = items.stream()
                .map(item -> {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderId(orderInfo.getId());

                    Long userId = AuthUtil.getUserAuth().getUserId();
                    detail.setUserId(userId);

                    detail.setSkuId(item.getSkuId());
                    detail.setSkuName(item.getSkuName());
                    detail.setImgUrl(item.getSkuDefaultImg());
                    detail.setOrderPrice(item.getSkuPrice());
                    detail.setSkuNum(item.getSkuNum());
                    detail.setHasStock("1");
                    detail.setCreateTime(new Date());
                    detail.setSplitTotalAmount(new BigDecimal("0"));
                    detail.setSplitActivityAmount(new BigDecimal("0"));
                    detail.setSplitCouponAmount(new BigDecimal("0"));


                    return detail;
                }).collect(Collectors.toList());

        return detailList;
    }


    /**
     * 准备orderInfo数据
     * @param vo
     * @return
     */
    private OrderInfo prepareOrderInfo(OrderSubmitVo vo) {
        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setConsignee(vo.getConsignee());
        orderInfo.setConsigneeTel(vo.getConsigneeTel());

        List<CartItemForOrderVo> detailList = vo.getOrderDetailList();
        BigDecimal totalAmount = detailList.stream()
                .map(item -> item.getOrderPrice().multiply(new BigDecimal(item.getSkuNum().toString())))
                .reduce((a, b) -> a.add(b))
                .get();

        orderInfo.setTotalAmount(totalAmount);

        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());

        UserAuthTo auth = AuthUtil.getUserAuth();
        orderInfo.setUserId(auth.getUserId());

        orderInfo.setPaymentWay(PaymentWay.ONLINE.name());

        orderInfo.setDeliveryAddress(vo.getDeliveryAddress());

        orderInfo.setOrderComment(vo.getOrderComment());

        //对外交易号
        String random = UUID.randomUUID().toString().substring(0, 5);
        //提前生成
        orderInfo.setOutTradeNo("GMALL-"+System.currentTimeMillis()+"-"+auth.getUserId()+"-"+random);//同一用户,同一时刻,最大5000万并发

        //交易体: 所有购买的商品名
        String skuNames = detailList.stream().map(CartItemForOrderVo::getSkuName)
                .reduce((a, b) -> a + ";" + b)
                .get();
        orderInfo.setTradeBody(skuNames);


        orderInfo.setCreateTime(new Date());

        //过期时间 30分钟
        long time = System.currentTimeMillis() + 1000 * 60 * 30;
        orderInfo.setExpireTime(new Date(time));

        orderInfo.setProcessStatus(ProcessStatus.UNPAID.name());

        //物流追踪号
        orderInfo.setTrackingNo("");

        //拆单: 父子订单
        orderInfo.setParentOrderId(0L);

        orderInfo.setImgUrl(detailList.get(0).getImgUrl()); //订单展示的图片

//        orderInfo.setOrderDetailList();

        orderInfo.setWareId("");
        orderInfo.setProvinceId(0L);

        orderInfo.setActivityReduceAmount(new BigDecimal("0"));
        orderInfo.setCouponAmount(new BigDecimal("0"));
        orderInfo.setOriginalTotalAmount(new BigDecimal("0"));
        //可退款7天
        orderInfo.setRefundableTime(null);
        orderInfo.setFeightFee(new BigDecimal("0"));
        orderInfo.setOperateTime(new Date());

//        orderInfo.setId(0L);

        return orderInfo;
    }
}
