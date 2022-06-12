package com.atguigu.gmall.order;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * 1、导包
 *        <dependency>
 *             <groupId>org.springframework.boot</groupId>
 *             <artifactId>spring-boot-starter-validation</artifactId>
 *         </dependency>
 * 2、给vo标注校验注解
 * 3、开启校验;
 *      在所有Controller接受请求参数的时候，使用 @Valid 开启数据校验
 *
 *
 * ================mq=============
 * 1、导包
 * 2、自动配置 RabbitAutoConfiguration ;
 *      1)、和rabbit有关的所有配置 RabbitProperties
 *      2)、RabbitTemplate : 操作RabbitMQ的。 添加监听器
 *      3)、RabbitMessagingTemplate： 只收发消息
 *      4)、AmqpAdmin：  交换机、绑定关系、队列的crud操作
 * 总结： 收发消息 RabbitTemplate；  操作MQ的交换机等底层用 AmqpAdmin；
 *
 */
@SpringCloudApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }

}
