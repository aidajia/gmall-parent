package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.cron.SkuIdBloomTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/product")
@RestController
public class SkuBloomController {

    @Autowired
    SkuIdBloomTask skuIdBloomTask;

    @GetMapping("/rebuild")
    public Result rebuildBloom(){
        skuIdBloomTask.rebuildbloom();
        return Result.ok();
    }

}
