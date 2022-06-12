package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
    implements OrderInfoService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Override
    public void updateOrderStatus(ProcessStatus originStatus, ProcessStatus modifyStatus, Long orderId, Long userId) {

        OrderInfo orderInfo = new OrderInfo();
        //设置订单最新状态
        orderInfo.setOrderStatus(modifyStatus.getOrderStatus().name());
        orderInfo.setProcessStatus(modifyStatus.name());

        orderInfo.setId(orderId);
        orderInfo.setUserId(userId);

        orderInfoMapper.updateOrderStatus(originStatus.name(),orderInfo);
    }

    @Override
    public void updateOrderStatusToPaid(String outTradeNo, long userId, String processStatus, String orderStatus) {
        orderInfoMapper.updateOrderStatusToPaid(outTradeNo,userId,processStatus,orderStatus);
    }
}




