package com.atguigu.gmall.product.cron;


import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.starter.redisson.SkuBloomTask;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SkuIdBloomTask implements SkuBloomTask {

    @Autowired
    SkuInfoService skuInfoService;

    @Qualifier("skuIdBloom")
    @Autowired
    RBloomFilter<Object> skuIdBloom;


    @Scheduled(cron = "0 0 3 * * 3")
    public void rebuildbloom(){
        //重建布隆
        log.info("系统正在重建sku布隆");
        skuIdBloom.delete();
        skuIdBloom.tryInit(5000000,0.0000001);
        initData(skuIdBloom);
    }

    /**
     * 初始化sku的布隆过滤器
     * 1. 去数据库查询到所有的skuId, 然后添加到布隆中
     */
    @Override
    public void initData(RBloomFilter<Object> skuIdBloom) {
        log.info("系统正在初始化sku布隆");
        //1.查出所有的skuId
        List<Long> ids = skuInfoService.getAllSkuIds();
        //2.放到布隆过滤器中
        for (Long id : ids) {
            skuIdBloom.add(id);
        }
    }
}
