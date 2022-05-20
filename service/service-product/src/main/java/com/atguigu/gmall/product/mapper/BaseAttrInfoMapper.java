package com.atguigu.gmall.product.mapper;


import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.atguigu.gmall.product.domain.BaseAttrInfo
 */
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {


    //根据分类id查询对应的所有平台属性的名和值
    List<BaseAttrInfo> selectAttrInfoAndAttrValueByCategoryId(@Param("c1Id") Long c1Id,
                                                              @Param("c2Id") Long c2Id,
                                                              @Param("c3Id") Long c3Id);

    BaseAttrInfo findAttrInfoAndAttrValueByAttrId(Long attrId);

    List<BaseAttrValue> findAttrValueByAttrId(Long attrId);
}




