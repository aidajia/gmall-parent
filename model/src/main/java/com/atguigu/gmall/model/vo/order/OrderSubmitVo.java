package com.atguigu.gmall.model.vo.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderSubmitVo {

    private String consignee; //收件人
    private String consigneeTel; //收件人电话
    private String deliveryAddress; //送货地址
    private String paymentWay; //支付方式
    private String orderComment; //订单说明
    private List<CartItemForOrderVo> orderDetailList; //订单商品详情

}
