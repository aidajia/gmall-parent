package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;

import java.util.List;

public interface BaseCategoryService {

    List<BaseCategory1> getAllCategory1();

    //查询出一个一级分类下的所有二级分类
    List<BaseCategory2> getCategory2ByC1Id(Long category1Id);

    //根据二级分类id查询出二级分类下的所有三级分类
    List<BaseCategory3> getCategory3ByC2Id(Long category2Id);

}
