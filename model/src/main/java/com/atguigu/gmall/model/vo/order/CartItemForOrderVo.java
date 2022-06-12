package com.atguigu.gmall.model.vo.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemForOrderVo {

    private String imgUrl;
    private String skuName;
    private BigDecimal orderPrice; //订单价格
    private Integer skuNum;
    private String stock; //是否有货 0:无货 1:有货

}
