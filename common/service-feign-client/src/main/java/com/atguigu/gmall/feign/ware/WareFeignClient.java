package com.atguigu.gmall.feign.ware;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(value = "ware-manage",url = "http://www.baidu.com")
@FeignClient(value = "ware-manage",url = "${app.props.ware-url}")
public interface WareFeignClient {


    /**
     * 检查一个商品的库存
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping("/hasStock")
    String hasStock(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num);

}
