package com.atguigu.gmall.order.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 网关 凡是带auth路径的必须登录
 */
@RestController
@RequestMapping("/api/order/auth")
public class OrderRestController {

    @Autowired
    OrderService orderService;

    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestParam("tradeNo") String tradeNo,
                              @RequestBody OrderSubmitVo orderSubmitVo){

        //提交订单
        Long orderId = orderService.submitOrder(tradeNo, orderSubmitVo);

        //TODO 删除购物车中选中的商品

        return Result.ok(orderId.toString());

    }

}
