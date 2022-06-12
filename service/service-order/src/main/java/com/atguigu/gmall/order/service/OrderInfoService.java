package com.atguigu.gmall.order.service;


import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 原子修改订单状态。
     * @param originStatus  期望订单原始状态是什么
     * @param modifyStatus  最终将订单修改成什么
     * @param orderId  订单id
     * @param userId   用户id
     */
    void updateOrderStatus(ProcessStatus originStatus, ProcessStatus modifyStatus, Long orderId, Long userId);

    /**
     * 修改订单为已支付
     * @param outTradeNo
     * @param userId
     * @param processStatus
     * @param orderStatus
     */
    void updateOrderStatusToPaid(String outTradeNo, long userId, String processStatus, String orderStatus);
}
