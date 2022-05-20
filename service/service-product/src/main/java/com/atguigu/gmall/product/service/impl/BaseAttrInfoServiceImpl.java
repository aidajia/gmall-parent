package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService{

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    //根据分类id查询对应的所有平台属性的名和值
    @Override
    public List<BaseAttrInfo> findAttrInfoAndAttrValueByCategoryId(Long c1Id, Long c2Id, Long c3Id) {

        return baseAttrInfoMapper.selectAttrInfoAndAttrValueByCategoryId(c1Id,c2Id,c3Id);
    }

    @Transactional
    @Override
    public void saveAttrInfoAndValue(BaseAttrInfo attrInfo) {
        //1. 保存属性名信息到 base_attr_info
        baseAttrInfoMapper.insert(attrInfo);

        //2. 保存属性值信息到 base_attr_value
        List<BaseAttrValue> attrValueList = attrInfo.getAttrValueList();
        for (BaseAttrValue value : attrValueList) {
            //回填属性id
            Long id = attrInfo.getId();//属性名的自增id
            value.setAttrId(id);
            baseAttrValueMapper.insert(value);
        }
    }

    @Transactional
    @Override
    public void updateAttrInfoAndValue(BaseAttrInfo attrInfo) {
        //1.修改属性名
        baseAttrInfoMapper.updateById(attrInfo);

        //3).从提交的数据里面, 对比发现数据库中原纪录没有提交的id记录, 这些id记录是要删除的
        //3.1 数据库中查原值
        // 删除 delete * from base_attr_value where attrId=12 and id not in(59 前端携带了的id)
        List<Long> nodel_vids = new ArrayList<>();
        for (BaseAttrValue value : attrInfo.getAttrValueList()) {
            if (value.getId() != null){
                nodel_vids.add(value.getId());
            }
        }
        if(nodel_vids.size() > 0){
            //有东西, 说明保留前端的部分数据
            QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("attr_id",attrInfo.getId());
            queryWrapper.notIn("id",nodel_vids);
            //所有属性值不在前端提交的范围内的都是不要的, 要删除
            baseAttrValueMapper.delete(queryWrapper);
        }else {
            QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("attr_id",attrInfo.getId());
            baseAttrValueMapper.delete(queryWrapper);
        }

        //2.修改属性值
        List<BaseAttrValue> valueList = attrInfo.getAttrValueList();
        for (BaseAttrValue value : valueList) {
            //1).有id的属性值, 直接改 base_attr_value
            if(value.getId() != null){
                baseAttrValueMapper.updateById(value);
            }

            //2).无id的属性值, 是新增 base_attr_value
            if(value.getId() == null){
                //新增要回填属性id
                value.setAttrId(attrInfo.getId());
                baseAttrValueMapper.insert(value);
            }



        }
    }

    @Override
    public BaseAttrInfo findAttrInfoAndAttrValueByAttrId(Long attrId) {

        return baseAttrInfoMapper.findAttrInfoAndAttrValueByAttrId(attrId);
    }
    //根据属性id返回属性所有值
    @Override
    public List<BaseAttrValue> findAttrValueByAttrId(Long attrId) {
        return baseAttrInfoMapper.findAttrValueByAttrId(attrId);
    }

    @Transactional
    @Override
    public void saveOrUpdateAttrInfo(BaseAttrInfo attrInfo) {
        Long id = attrInfo.getId();
        if(id == null){
            //前端没有提交属性id, 则是新增属性
            saveAttrInfoAndValue(attrInfo);
        }else {
            //前端提交了属性id, 则是修改属性
            updateAttrInfoAndValue(attrInfo);
        }
    }
}




