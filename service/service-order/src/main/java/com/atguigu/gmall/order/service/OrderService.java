package com.atguigu.gmall.order.service;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;

public interface OrderService {
    OrderConfirmVo getOrderConfirmData();

    /**
     * 生成一个 交易号 [防重令牌]
     * @return
     */
    String generateTradeNo();

    /**
     * 校验防重令牌
     * @return
     */
    boolean checkTradeNo(String token);

    /**
     * 提交订单 返回订单Id
     * @param tradeNo
     * @param orderSubmitVo
     */
    Long submitOrder(String tradeNo, OrderSubmitVo orderSubmitVo);

    /**
     * 保存订单
     * @param orderSubmitVo
     */
    Long saveOrder(OrderSubmitVo orderSubmitVo);

    /**
     * 订单创建完成后,发送消息
     * @param orderId
     */
    void sendOrderCreateMsg(Long orderId);

    /**
     * 获取一个订单信息
     * @param orderId
     * @return
     */
    OrderInfo getOrderInfoIdAndAmount(Long orderId);

    /**
     * 修改订单变成已支付
     * @param outTradeNo
     */
    void updateOrderStatusToPAID(String outTradeNo);

    /**
     * 检查指定订单的状态, 是否和支付宝中订单的支付状态是否一致,不一致则同步
     * @param outTradeNo
     */
    void checkAndSyncOrderStatus(String outTradeNo);
}
