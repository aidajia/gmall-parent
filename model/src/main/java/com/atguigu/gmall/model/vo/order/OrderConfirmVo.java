package com.atguigu.gmall.model.vo.order;

import com.atguigu.gmall.model.user.UserAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderConfirmVo {

    //imgUrl、skuName、orderPrice[实时价格]、skuNum
    private List<CartItemForOrderVo> detailArrayList;
    private Integer totalNum;
    private BigDecimal totalAmount;
    private List<UserAddress> userAddressList;
    private String tradeNo;

}
