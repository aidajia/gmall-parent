package com.atguigu.gmall.feign.order;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/rpc/inner/order")
@FeignClient("service-order")
public interface OrderFeignClient {

    /**
     * 返回订单确认页数据
     * 1. feign帮我们发送请求, 要到数据
     * 2. feign帮我们转成指定的返回值类型
     *
     * 我们完成可以告诉Feign, 不用转成精确类型, 只需要转成map(json-vo),
     *
     * @return
     */
    @GetMapping("/confirm")
    Result<Map<String,Object>> getOrderConfirmData();

    /**
     * 获取一个订单信息
     * @param orderId
     * @return
     */
    @GetMapping("/info/{id}")
    Result<OrderInfo> getOrderInfoByUserId(@PathVariable("id") Long orderId);


    /**
     * 修改订单为已支付
     * @param outTradeNo
     * @return
     */
    @GetMapping("/status/update/paid/{outTradeNo}")
    Result updateOrderStatusToPAID(@PathVariable("outTradeNo") String outTradeNo);

    /**
     * 检查数据库此订单是否已经同步了支付宝订单的状态
     * @param outTradeNo
     * @return
     */
    @GetMapping("/status/checkandsync/{outTradeNo}")
    Result checkOrderStatus(@PathVariable("outTradeNo") String outTradeNo);

}
