package com.atguigu.gmall.product.rpc;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rpc/inner/product")
public class SkuRpcController {

    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImageService skuImageService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    /**
     * 查询skuinfo信息
     * @param skuId
     * @return
     */
    @GetMapping("/skuinfo/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId){

        //1. 查询skuInfo
        SkuInfo info = skuInfoService.getById(skuId);
        //2. 查询skuImageList
        List<SkuImage> skuImages = skuImageService.list(new QueryWrapper<SkuImage>().eq("sku_id", skuId));

        info.setSkuImageList(skuImages);
        return Result.ok(info);
    }

    /**
     * 查询商品价格
     * @param skuId
     * @return
     */
    @GetMapping("/skuinfo/price/{skuId}")
    public Result<BigDecimal> getSkuPrice(@PathVariable("skuId") Long skuId){

        BigDecimal bigDecimal = skuInfoService.getSkuPrice(skuId);

        return Result.ok(bigDecimal);
    }

    /**
     * 查询指定sku对象的spu对应的销售属性名和值
     * @param skuId
     * @return
     */
    @GetMapping("/skuinfo/spu/saleattrandvalues/{skuId}")
    public Result<List<SpuSaleAttr>> getSkuDeSpuSaleAttrAndValue(@PathVariable("skuId") Long skuId){
        List<SpuSaleAttr> saleAttrs = skuInfoService.getSkuDeSpuSaleAttrAndValue(skuId);
        return Result.ok(saleAttrs);
    }
//skuSaleAttrValueList

    /**
     * 查询skuValueJson数据
     * @param skuId
     * @return
     */
    @GetMapping("/skuinfo/valuejson/{skuId}")
    public Result<Map<String,String>> getSkuValueJson(@PathVariable("skuId") Long skuId){
        Map<String,String> valueJson = skuSaleAttrValueService.getSaleAttrValueList(skuId);
        return Result.ok(valueJson);
    }

}
