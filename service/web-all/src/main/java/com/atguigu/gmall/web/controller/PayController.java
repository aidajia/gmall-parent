package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.model.order.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 支付
 */
@Controller
public class PayController {

    @Autowired
    OrderFeignClient orderFeignClient;

    @GetMapping("/pay.html")
    public String payPage(@RequestParam("orderId") Long orderId,
                          Model moder){

        //TODO 数据
        //orderInfo (id, totalAmount)
        Result<OrderInfo> orderInfoByUserId = orderFeignClient.getOrderInfoByUserId(orderId);

        moder.addAttribute("orderInfo",orderInfoByUserId.getData());

        return "payment/pay";
    }

    @GetMapping("/pay/success.html")
    public String paySuccess(@RequestParam("out_trad_no") String out_trad_no){

        //1.查订单的状态. 如果还是 UNPAID ,就可以通知后台自己去改订单
        orderFeignClient.checkOrderStatus(out_trad_no);

        return "payment/success";
    }

}
