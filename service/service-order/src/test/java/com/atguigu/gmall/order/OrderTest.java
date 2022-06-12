package com.atguigu.gmall.order;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest
public class OrderTest {

    @Autowired
    OrderInfoMapper orderInfoMapper;

//    @Test
//    public void saveOrder(){
//        OrderInfo orderInfo = new OrderInfo();
//        orderInfo.setConsignee("真正");
//        orderInfo.setConsigneeTel("xxx");
//        orderInfo.setTotalAmount(new BigDecimal("11"));
//        orderInfo.setOrderStatus("11");
//        orderInfo.setUserId(4L);
//        orderInfo.setPaymentWay("11");
//        orderInfo.setDeliveryAddress("11");
//        orderInfo.setOrderComment("11");
//        orderInfo.setOutTradeNo("333");
//        orderInfo.setTradeBody("444");
//        orderInfo.setCreateTime(new Date());
//        orderInfo.setExpireTime(new Date());
//        orderInfo.setProcessStatus("rrr");
//        orderInfo.setTrackingNo("ggg");
//        orderInfo.setParentOrderId(0L);
//        orderInfo.setImgUrl("fff");
//        orderInfo.setOrderDetailList(Lists.newArrayList());
//        orderInfo.setWareId("ss");
//        orderInfo.setProvinceId(0L);
//        orderInfo.setActivityReduceAmount(new BigDecimal("0"));
//        orderInfo.setCouponAmount(new BigDecimal("0"));
//        orderInfo.setOriginalTotalAmount(new BigDecimal("0"));
//        orderInfo.setRefundableTime(new Date());
//        orderInfo.setFeightFee(new BigDecimal("0"));
//        orderInfo.setOperateTime(new Date());
//
//        orderInfoMapper.insert(orderInfo);
//    }

}
