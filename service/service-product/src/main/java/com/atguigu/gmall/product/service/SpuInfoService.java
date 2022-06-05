package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.SpuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface SpuInfoService extends IService<SpuInfo> {

    /**
     * 保存spu信息
     * @param spuInfo 前端提交来的数据
     */
    void saveSpuInfo(SpuInfo spuInfo);
}
