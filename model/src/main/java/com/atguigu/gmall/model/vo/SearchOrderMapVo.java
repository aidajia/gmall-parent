package com.atguigu.gmall.model.vo;


import lombok.Data;

@Data
public class SearchOrderMapVo {

    private String type; //排序方式: 1综合 或者 2价格
    private String sort; //排序规则

}
