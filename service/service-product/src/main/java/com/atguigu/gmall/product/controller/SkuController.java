package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/product")
@RestController
public class SkuController {

    @Autowired
    SkuInfoService skuInfoService;

    @GetMapping("/list/{page}/{limit}")
    public Result getSkuInfo(@PathVariable("page") Long page,
                             @PathVariable("limit") Long limit){

        Page<SkuInfo> skuInfoPage = skuInfoService.page(new Page<SkuInfo>(page, limit));
        return Result.ok(skuInfoPage);
    }
    /**
     * sku信息保存
     */
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){

        //1. SkuInfo 的基本信息 保存到 sku_info
        //2. skuAttrValueList  sku的平台属性 保存到 sku_attr_value.前端提交的就够了

        //3. skuSaleAttrValueList sku销售属性值集合 存到 sku_sale_attr_value
        // (只需要保存 saleAttrValueId 和 sku的关联关系)
        //
        //4. skuImageList sku图片集合: 存到sku_image表
        skuInfoService.saveSkuInfo(skuInfo);

        return Result.ok();
    }

    /**
     * 上架
     * @param skuId
     * @return
     */
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId){
        skuInfoService.upOrDownSku(skuId,1);

        return Result.ok();
    }

    /**
     * 下架
     * @param skuId
     * @return
     */
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSal(@PathVariable("skuId") Long skuId){
        skuInfoService.upOrDownSku(skuId,0);

        return Result.ok();
    }
}
