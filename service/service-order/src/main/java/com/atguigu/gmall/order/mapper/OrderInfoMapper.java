package com.atguigu.gmall.order.mapper;


import com.atguigu.gmall.model.order.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.atguigu.gmall.order.domain.OrderInfo1
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    void updateOrderStatus(@Param("originStatus") String originStatus,
                           @Param("orderInfo") OrderInfo orderInfo);

    void updateOrderStatusToPaid(@Param("outTradeNo") String outTradeNo,
                                 @Param("userId") Long userId,
                                 @Param("processStatus") String processStatus,
                                 @Param("orderStatus") String orderStatus);
}




