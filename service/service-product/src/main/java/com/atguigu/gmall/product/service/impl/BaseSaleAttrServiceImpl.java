package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.starter.cache.aop.annotation.Cache;
import com.atguigu.gmall.common.constants.RedisConst;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.mapper.SpuSaleAttrValueMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.BaseSaleAttrService;
import com.atguigu.gmall.product.mapper.BaseSaleAttrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class BaseSaleAttrServiceImpl extends ServiceImpl<BaseSaleAttrMapper, BaseSaleAttr>
    implements BaseSaleAttrService{

    @Autowired
    SpuSaleAttrValueMapper saleAttrValueMapper;

    @Cache(cacheKey = RedisConst.SALE_ATTR_CACHE_KEY)
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrAndValue(Long spuId) {
        List<SpuSaleAttr> spuSaleAttrs = saleAttrValueMapper.getSpuSaleAttrAndValue(spuId);
        return spuSaleAttrs;
    }
}




