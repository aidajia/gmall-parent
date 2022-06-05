package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 销售属性:
 * 1. base_sale_attr 销售属性名表
 * 2. 销售属性值以及销售属性和商品的关联关系在其他表
 *      base_sale_attr      销售属性名表
 *      sku_sale_attr_value     sku具体的销售属性值信息
 *      spu_sale_attr       spu销售属性表
 *      spu_sale_attr_value     spu销售属性值表
 */
@RequestMapping("/admin/product")
@RestController
public class SaleAttrController {

    @Autowired
    BaseSaleAttrService baseSaleAttrService;

    /**
     * 获取系统所有的销售属性列表
     * @return
     */
    @GetMapping("/baseSaleAttrList")
    public Result getSaleAttrList(){

        List<BaseSaleAttr> list = baseSaleAttrService.list();
        return Result.ok(list);
    }

    //获取指定spu定义的所有销售属性名和值
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result spuSaleAttrList(@PathVariable("spuId") Long spuId){

        List<SpuSaleAttr> attrs = baseSaleAttrService.getSpuSaleAttrAndValue(spuId);
        return Result.ok(attrs);
    }

}
