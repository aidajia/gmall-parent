package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Slf4j
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService{

    @Autowired
    SpuInfoMapper spuInfoMapper;
    @Autowired
    SpuImageService spuImageService;
    @Autowired
    SpuSaleAttrService spuSaleAttrService;
    @Autowired
    SpuSaleAttrValueService spuSaleAttrValueService;

    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        log.info("保存spu: {}",spuInfo);
        //spuinfo 保存到 spu_info表中
        spuInfoMapper.insert(spuInfo);
        Long id = spuInfo.getId();

        //spuImageList 保存到spu_image表中
        List<SpuImage> imageList = spuInfo.getSpuImageList();
        for (SpuImage image : imageList) {
            image.setSpuId(id);
        }
        spuImageService.saveBatch(imageList);

        //spuSaleAttrList 保存到 spu_sale_attr_value
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
            spuSaleAttr.setSpuId(id);//回填spuId
            //销售属性名和spu的关联关系保存完成
            spuSaleAttrService.save(spuSaleAttr);

            //当前销售属性提交的所有值
            List<SpuSaleAttrValue> valueList = spuSaleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue value : valueList) {
                value.setSpuId(id);
                value.setSaleAttrName(spuSaleAttr.getSaleAttrName());
            }

            //保存所有销售属性值
            spuSaleAttrValueService.saveBatch(valueList);

        }
    }
}




