package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.dao.BaseCategory1Dao;
import com.atguigu.gmall.product.dao.BaseCategory2Dao;
import com.atguigu.gmall.product.dao.BaseCategory3Dao;
import com.atguigu.gmall.product.service.BaseCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class BaseCategoryServiceImpl implements BaseCategoryService {

    @Resource
    BaseCategory1Dao baseCategory1Dao;
    @Resource
    BaseCategory2Dao baseCategory2Dao;
    @Resource
    BaseCategory3Dao baseCategory3Dao;

    @Override
    public List<BaseCategory1> getAllCategory1() {
        List<BaseCategory1> category1s = baseCategory1Dao.selectList(null);
        return category1s;
    }

    /**
     * 查询出一个一级分类下的所有二级分类
     * @param category1Id
     * @return
     */
    @Override
    public List<BaseCategory2> getCategory2ByC1Id(Long category1Id) {
        QueryWrapper<BaseCategory2> wrapper = new QueryWrapper<>();
        wrapper.eq("category1_id",category1Id);
        List<BaseCategory2> baseCategory2s = baseCategory2Dao.selectList(wrapper);
        return baseCategory2s;
    }

    @Override
    public List<BaseCategory3> getCategory3ByC2Id(Long category2Id) {
        QueryWrapper<BaseCategory3> wrapper = new QueryWrapper<>();
        wrapper.eq("category2_id",category2Id);
        List<BaseCategory3> baseCategory3s = baseCategory3Dao.selectList(wrapper);
        return baseCategory3s;
    }
}
