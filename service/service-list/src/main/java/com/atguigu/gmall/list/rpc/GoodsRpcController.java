package com.atguigu.gmall.list.rpc;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.list.service.GoodsSearchService;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/rpc/inner/es")
@RestController
public class GoodsRpcController {

    @Autowired
    GoodsSearchService goodsSearchService;

    /**
     * 上架商品, 保存数据到ES
     * @param goods
     * @return
     */
    @PostMapping("/goods/save")
    public Result saveGoods(@RequestBody Goods goods){

        goodsSearchService.saveGoods(goods);

        return Result.ok();
    }


    /**
     * 下架商品, 从ES中删除数据
     * @param skuId
     * @return
     */
    @GetMapping("/goods/delete/{skuId}")
    public Result deleteGoods(@PathVariable("skuId") Long skuId){

        goodsSearchService.deleteGoods(skuId);

        return Result.ok();
    }

    /**
     * 远程检索商品
     * @param param
     * @return
     */
    @PostMapping("/goods/search")
    public Result<GoodsSearchResultVo> searchGoods(@RequestBody SearchParam param,
                                                   HttpServletRequest request){
        //获取url?后面的所有东西[查询字符串]

        // 检索
        GoodsSearchResultVo vo = goodsSearchService.search(param);

        
        return Result.ok(vo);
    }

    /**
     * 增加某个skuId对应商品的热度
     * @param skuId
     * @return
     */
    @GetMapping("/goods/incrHotScore/{skuId}")
    public Result updateHotScore(@PathVariable("skuId") Long skuId,
                               @RequestParam("hotScore") Long score){
        goodsSearchService.updateHotScore(skuId,score);
        return Result.ok();
    }

}
