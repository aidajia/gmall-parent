package com.atguigu.gmall.model.to;

import lombok.Data;

import java.util.List;

@Data
public class CategoryAndChildTo {
    Long categoryId; //当前分类的id
    String categoryName; //当前分类的名字
    List<CategoryAndChildTo> categoryChild; //当前分类的子分类
}
