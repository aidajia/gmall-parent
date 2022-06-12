package com.atguigu.gmall.order;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

//    @Test
//    void testSend(){
//        rabbitTemplate.convertAndSend("hello","h1","哈哈哈");
//    }
//
//    @Test
//    void testReceive(){
//        //调一下收一个
//        Message message = rabbitTemplate.receive("haha");
//
//        //有消息属性(基本属性+自定义头)+消息体(内容)
//        MessageProperties properties = message.getMessageProperties();
//        System.out.println("消息属性 = " + properties);
//
//        byte[] body = message.getBody();
//        String str = new String(body);
//        System.out.println( str);
//
//    }

}
