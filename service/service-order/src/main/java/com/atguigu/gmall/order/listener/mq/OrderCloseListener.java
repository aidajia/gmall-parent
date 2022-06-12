package com.atguigu.gmall.order.listener.mq;

import com.atguigu.gmall.common.constants.MqConst;
import com.atguigu.gmall.common.util.JSONS;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.mqto.order.OrderCreateTo;
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
public class OrderCloseListener {

    @Autowired
    OrderInfoService orderInfoService;

    @RabbitListener(queues = MqConst.ORDER_DEAD_QUEUE)
    public void closeOrder(Message message, Channel channel){
        //1.拿到过期订单消息
        OrderCreateTo create = null;
        try{
            create = JSONS.strToObj(new String(message.getBody()), new TypeReference<OrderCreateTo>() {
            });

            //2、开始关闭订单
            Long orderId = create.getOrderId();
            Long userId = create.getUserId();
            //查询
//        OrderInfo byId = orderInfgetBodyoService.getById(orderId);
//        if(byId.getOrderStatus().equals("UNPAID")){  //千万不要这么写

//        }

            //原子改 关 保证不会对订单进行误操作
            //update order_info set order_status="CLOSED",process_status="CLOSED" where id=? and user_id=? and order_status='UNPAID'
            //cas(期望、修改)
            //具有幂等性。
            orderInfoService.updateOrderStatus(ProcessStatus.UNPAID,ProcessStatus.CLOSED,orderId,userId);
            log.info("订单已处理：{}",create);
        }catch (Exception e){
            log.error("订单关闭发送错误。错误：{}，订单信息：{}",e,create);
        }


        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            //走到这里,说明和mq的长连接断了
            log.error("mq发生异常: 错误信息: {}, 订单关单消息可能处理失败:消息内容: {}",e,create);

        }

    }

}
