package com.atguigu.gmall.list.service;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;

public interface GoodsSearchService {
    /**
     * 保存商品数据到es中
     * @param goods
     */
    void saveGoods(Goods goods);

    /**
     * 删除es中的商品数据
     * @param skuId
     */
    void deleteGoods(Long skuId);

    /**
     * 检索
     * @param param
     * @return
     */
    GoodsSearchResultVo search(SearchParam param);

    void updateHotScore(Long skuId, Long score);
}
