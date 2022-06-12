package com.atguigu.gmall.order.listener.mq;

import com.atguigu.gmall.common.constants.MqConst;
import com.atguigu.gmall.common.util.JSONS;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.mqto.order.PayNotifySuccessVo;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class OrderPayedListener {

    @Autowired
    OrderInfoService orderInfoService;

    @RabbitListener(queues = MqConst.ORDER_PAYED_QUEUE)
    public void listenPayedOrder(Message message, Channel channel){

        try {
            //执行业务
            PayNotifySuccessVo obj = JSONS.strToObj(new String(message.getBody()), new TypeReference<PayNotifySuccessVo>() {
            });
            log.info("监听到成功支付的订单: {}",obj.getOut_trade_no());

            //修改订单为已支付状态
            //String outTradeNo,
            // long userId,
            // String processStatus,
            // String orderStatus
            Long userId = Long.parseLong(obj.getOut_trade_no().split("-")[2]);
            if("TRADE_SUCCESS".equals(obj.getTrade_status())){
                ProcessStatus paid = ProcessStatus.PAID;
                log.info("修改订单状态为已支付: {}",obj.getOut_trade_no());
                orderInfoService.updateOrderStatusToPaid(obj.getOut_trade_no(),userId,paid.name(),paid.getOrderStatus().name());
            }

            //回复消费成功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {

        } finally {

        }

    }

}
