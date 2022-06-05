package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * 商品上下架
     * @param skuId
     * @param status
     */
    void upOrDownSku(Long skuId, int status);

    /**
     * 查询商品价格
     * @param skuId
     * @return
     */
    BigDecimal getSkuPrice(Long skuId);

    /**
     * 查询指定sku对象的spu对应的销售属性名和值
     * @param skuId
     * @return
     */
    List<SpuSaleAttr> getSkuDeSpuSaleAttrAndValue(Long skuId);

    /**
     * 查出所有的skuId
     * @return
     */
    List<Long> getAllSkuIds();

    /**
     * 查询商品信息封装为es需要的数据
     * @param skuId
     * @return
     */
    Goods getSkuInfoForSearch(Long skuId);

//    List<SpuSaleAttr> getSaleAttrValueList(Long skuId);
}
