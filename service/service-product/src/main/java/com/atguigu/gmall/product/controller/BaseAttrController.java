package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 处理和平台属性有关的请求
 */
//
@Slf4j
@RequestMapping("/admin/product")
@RestController
public class BaseAttrController {

    @Autowired
    BaseAttrInfoService baseAttrInfoService;

    /**
     * 根据分类id查询对应的所有平台属性的名和值
     * @param c1Id
     * @param c2Id
     * @param c3Id
     * @return
     */
    @GetMapping("/attrInfoList/{c1Id}/{c2Id}/{c3Id}")
    public Result getAttrInfoList(@PathVariable("c1Id") Long c1Id,
                                  @PathVariable("c2Id") Long c2Id,
                                  @PathVariable("c3Id") Long c3Id){

        List<BaseAttrInfo> baseAttrInfos = baseAttrInfoService.findAttrInfoAndAttrValueByCategoryId(c1Id,c2Id,c3Id);

        return Result.ok(baseAttrInfos);
    }

    /**
     * 保存/修改平台属性
     * @param attrInfo
     * @return
     */
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo attrInfo){
        log.info("保存/修改平台属性:{}",attrInfo);
        //baseAttrInfoService.saveAttrInfoAndValue(attrInfo);

        baseAttrInfoService.saveOrUpdateAttrInfo(attrInfo);
        return Result.ok();
    }

    /**
     * 根据属性id返回属性所有值
     * @param attrId
     * @return
     */
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId") Long attrId){

        //BaseAttrInfo info = baseAttrInfoService.findAttrInfoAndAttrValueByAttrId(attrId);
        List<BaseAttrValue> values = baseAttrInfoService.findAttrValueByAttrId(attrId);
        return Result.ok(values);
    }

}
