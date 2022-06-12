package com.atguigu.gmall.feign.pay;

import com.atguigu.gmall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/rpc/inner/pay")
@FeignClient("service-pay")
public interface PayFeignClient {
    /**
     * 查询某次交易详情
     * @param outTradeNo
     * @return
     */
    @GetMapping("/query/{outTradeNo}")
    Result<String> queryTrade(@PathVariable("outTradeNo") String outTradeNo);
}
