package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseSaleAttrService extends IService<BaseSaleAttr> {
    /**
     * 查询supId对应的所有销售属性名和值
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrAndValue(Long spuId);
}
