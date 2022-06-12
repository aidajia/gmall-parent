package com.atguigu.gmall.order.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrVo {

    private String fieldName;
    private String message;
}
