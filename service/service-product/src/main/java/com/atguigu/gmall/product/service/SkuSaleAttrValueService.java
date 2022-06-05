package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 *
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValue> {

    /**
     * 查询当前sku以及兄弟们的所有销售属性组合
     * @param skuId
     * @return
     */
    Map<String, String> getSaleAttrValueList(Long skuId);
}
