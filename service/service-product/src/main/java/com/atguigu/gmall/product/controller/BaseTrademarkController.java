package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//
@RestController
@RequestMapping("/admin/product")
public class BaseTrademarkController {

    @Autowired
    BaseTrademarkService baseTrademarkService;

    /**
     * 分页查询所有品牌
     */
    @GetMapping("/baseTrademark/{page}/{limit}")
    public Result getBaseTrademarkPage(@PathVariable("page") Long page,
                                       @PathVariable("limit") Long limit){
        Page<BaseTrademark> baseTrademarkPage = new Page<>(page, limit);

        //调用分页查询方法
        Page<BaseTrademark> result = baseTrademarkService.page(baseTrademarkPage);

        //前端全量接收分页数据以及查到的结果
        return Result.ok(result);
    }

    //admin/product/baseTrademark/save
    @PostMapping("/baseTrademark/save")
    public Result saveBaseTrademark(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    /**
     * 查询品牌
     * @param id
     * @return
     */
    //baseTrademark/get/{id}
    @GetMapping("/baseTrademark/get/{id}")
    public Result getBaseTrademarkById(@PathVariable("id") Long id){
        BaseTrademark trademark = baseTrademarkService.getById(id);
        return Result.ok(trademark);
    }

    /**
     * 修改品牌
     * @param baseTrademark
     * @return
     */
    @PutMapping("/baseTrademark/update")
    public Result updateBaseTrademark(@RequestBody BaseTrademark baseTrademark){
         baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    //baseTrademark/remove/{id}
    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result deleteBaseTrademark(@PathVariable("id") Long id){
        baseTrademarkService.removeById(id);
        return Result.ok();
    }

}
