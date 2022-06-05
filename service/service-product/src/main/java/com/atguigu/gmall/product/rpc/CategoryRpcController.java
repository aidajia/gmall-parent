package com.atguigu.gmall.product.rpc;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.atguigu.gmall.product.service.BaseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rpc/inner/product")
public class CategoryRpcController {

    @Autowired
    BaseCategoryService baseCategoryService;

    /**
     * 获取系统的所有分类以及子分类
     * @return
     */
    @GetMapping("/categorys")
    public Result<List<CategoryAndChildTo>> getAllCategoryWithChilds(){
        List<CategoryAndChildTo> categorys = baseCategoryService.getAllCategoryWithChilds();
        return Result.ok(categorys);
    }

    /**
     * 获取一个sku的分类层级信息
     * @param skuId
     * @return
     */
    @GetMapping("/category/view/{skuId}")
    public Result<BaseCategoryView> getSkuCategoryView(
            @PathVariable("skuId") Long skuId){

        BaseCategoryView view = baseCategoryService.getSkuCategoryView(skuId);
        return Result.ok(view);
    }

}
