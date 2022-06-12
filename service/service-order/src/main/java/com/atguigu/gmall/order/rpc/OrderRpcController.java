package com.atguigu.gmall.order.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rpc/inner/order")
@RestController
public class OrderRpcController {

    @Autowired
    OrderService orderService;

    /**
     * 返回订单确认页数据
     * @return
     */
    @GetMapping("/confirm")
    public Result<OrderConfirmVo> getOrderConfirmData(){

        OrderConfirmVo confirmVo = orderService.getOrderConfirmData();
        return Result.ok(confirmVo);
    }

    /**
     * 获取一个订单信息
     * @param orderId
     * @return
     */
    @GetMapping("/info/{id}")
    public Result<OrderInfo> getOrderInfoByUserId(@PathVariable("id") Long orderId){

        OrderInfo orderInfo = orderService.getOrderInfoIdAndAmount(orderId);
        return Result.ok(orderInfo);
    }

    /**
     * 修改订单为已支付
     * @param outTradeNo
     * @return
     */
    @GetMapping("/status/update/paid/{outTradeNo}")
    public Result updateOrderStatusToPAID(@PathVariable("outTradeNo") String outTradeNo){

        orderService.updateOrderStatusToPAID(outTradeNo);
        return Result.ok();
    }

    /**
     * 检查数据库此订单是否已经同步了支付宝订单的状态
     * @param outTradeNo
     * @return
     */
    @GetMapping("/status/checkandsync/{outTradeNo}")
    public Result checkOrderStatus(@PathVariable("outTradeNo") String outTradeNo){

        orderService.checkAndSyncOrderStatus(outTradeNo);
        return Result.ok();
    }

}
