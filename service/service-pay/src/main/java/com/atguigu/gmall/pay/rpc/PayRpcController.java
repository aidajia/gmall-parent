package com.atguigu.gmall.pay.rpc;

import com.alipay.api.AlipayApiException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.pay.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rpc/inner/pay")
@RestController
public class PayRpcController {

    @Autowired
    AlipayService alipayService;

    /**
     * 查询某次交易详情
     * @param outTradeNo
     * @return
     * @throws AlipayApiException
     */
    @GetMapping("/query/{outTradeNo}")
    public Result<String> queryTrade(@PathVariable("outTradeNo") String outTradeNo) throws AlipayApiException {
        //查询交易
        String trade = alipayService.queryTrade(outTradeNo);

        return Result.ok(trade);
    }
}
