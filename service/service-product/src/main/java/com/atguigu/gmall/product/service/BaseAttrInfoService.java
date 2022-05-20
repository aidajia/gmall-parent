package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    //根据分类id查询对应的所有平台属性的名和值
    List<BaseAttrInfo> findAttrInfoAndAttrValueByCategoryId(Long c1Id, Long c2Id, Long c3Id);

    //保存平台属性名和值
    void saveAttrInfoAndValue(BaseAttrInfo attrInfo);

    //修改平台属性名和值
    void updateAttrInfoAndValue(BaseAttrInfo attrInfo);

    //根据属性id返回属性名和值
    BaseAttrInfo findAttrInfoAndAttrValueByAttrId(Long attrId);

    //根据属性id返回属性所有值
    List<BaseAttrValue> findAttrValueByAttrId(Long attrId);


    /**
     * 保存/修改平台属性
     * @param attrInfo
     */
    void saveOrUpdateAttrInfo(BaseAttrInfo attrInfo);
}
