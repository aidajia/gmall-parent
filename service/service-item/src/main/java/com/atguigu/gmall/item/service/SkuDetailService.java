package com.atguigu.gmall.item.service;

import com.atguigu.gmall.model.to.SkuDetailTo;

public interface SkuDetailService {

    /**
     * 查询skuId指定的sku详情数据
     * @param skuId
     * @return
     */
    SkuDetailTo getSkuDetail(Long skuId);

    /**
     * 增加当前skuId的热度
     * @param skuId
     */
    void incrHotScore(Long skuId);
}
