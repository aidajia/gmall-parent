package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

//  /admin/product/{page}/{limit}?category3Id=61
@RequestMapping("/admin/product")
@RestController
public class SpuController {

    @Autowired
    SpuInfoService spuInfoService;

    /**
     * 获取平台属性分页列表信息
     * @param page
     * @param limit
     * @param category3Id
     * @return
     */
    @GetMapping("/{page}/{limit}")
    public Result getSpuInfoPage(@PathVariable("page") Long page,
                                 @PathVariable("limit") Long limit,
                                 @RequestParam("category3Id") Long category3Id){

        Page<SpuInfo> infoPage = new Page<>(page,limit);
        //分页查询
        QueryWrapper<SpuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category3_id",category3Id);

        Page<SpuInfo> spuInfoPage = spuInfoService.page(infoPage, queryWrapper);
        return Result.ok(spuInfoPage);
    }

    @PostMapping("/saveSpuInfo")
    public Result saveSpu(@RequestBody SpuInfo spuInfo){
        spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

}
