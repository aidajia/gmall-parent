package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.BaseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//

@RequestMapping("admin/product")
@RestController
public class CategoryController {

    @Autowired
    BaseCategoryService baseCategoryService;

    @GetMapping("/getCategory1")
    public Result getCategory1(){

        //TODO 查询出所有的一级分类
        List<BaseCategory1> category1s = baseCategoryService.getAllCategory1();

        return Result.ok(category1s);
    }


    @GetMapping("getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable("category1Id") Long category1Id){

        //查询出一个一级分类下的所有二级分类
        List<BaseCategory2> category2s = baseCategoryService.getCategory2ByC1Id(category1Id);

        return Result.ok(category2s);
    }

    ///admin/product/getCategory3/{category2Id}
    @GetMapping("getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable("category2Id") Long category2Id){

        //根据二级分类id查询出二级分类下的所有三级分类
        List<BaseCategory3> category3s = baseCategoryService.getCategory3ByC2Id(category2Id);

        return Result.ok(category3s);
    }


}
