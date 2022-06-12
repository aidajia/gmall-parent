package com.atguigu.gmall.pay.service;

import com.alipay.api.AlipayApiException;

import java.util.Map;

public interface AlipayService {

    /**
     * 展示一个订单的收款页
     * @param orderId
     * @return
     */
    String payPage(Long orderId) throws AlipayApiException;

    /**
     * 验证签名
     */
    boolean checkSign(Map<String,String> params) throws AlipayApiException;

    /**
     * 查询交易详情
     * @param outTradeNo
     * @return 订单状态
     * @throws AlipayApiException
     */
    String queryTrade(String outTradeNo) throws AlipayApiException;
}
