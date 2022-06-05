package com.atguigu.gmall.starter.redisson;

import org.redisson.api.RBloomFilter;

public interface BloomTask {

    //给布隆过滤器初始化数据
    void initData(RBloomFilter<Object> skuIdBloom);
}
