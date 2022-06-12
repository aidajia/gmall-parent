package com.atguigu.gmall.pay.controller;

import com.alipay.api.AlipayApiException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.pay.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment/alipay")
public class AliPayController {

    @Autowired
    AlipayService alipayService;

    @GetMapping(value = "/submit/{orderId}",produces = "text/html;chartset=utf-8")
    public String pay(@PathVariable("orderId") Long orderId) throws AlipayApiException {

        //处理支付

        String result = alipayService.payPage(orderId);

        return result;
    }

}
