package com.atguigu.gmall.model.vo;


import lombok.Data;

import java.util.List;

@Data //属性名 属性值 属性id
public class SearchAttrListVo {

    private String attrName;
    private List<String> attrValueList; //检索到的所有商品 attrName 这种属性到底涉及到的所有值
    private Long attrId;

}
