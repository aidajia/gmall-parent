package com.atguigu.gmall.order.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class HelloListener {

    @RabbitListener(queues = "haha")
    public void listenHaha(Message message, Channel channel) throws InterruptedException {
        log.info("收到消息:{}",new String(message.getBody()));
        System.out.println("正在处理...");
        Thread.sleep(5000);
        System.out.println("处理完了...");

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            channel.basicAck(deliveryTag,false);
            System.out.println("回复成功");
        } catch (IOException e) {
            //这个消息没有回复成功
            log.error("队列断网,导致消息回复失败: deliveryTag:{}, content:{}",deliveryTag,new String(message.getBody()));
        }
    }

}
