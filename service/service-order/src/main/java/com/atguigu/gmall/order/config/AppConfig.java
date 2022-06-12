package com.atguigu.gmall.order.config;

import com.atguigu.gmall.annotation.EnableAppDoubleThreadPool;
import com.atguigu.gmall.annotation.EnableAutoHandleException;
import com.atguigu.gmall.annotation.EnableFeignAuthHeaderInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients(basePackages = {"com.atguigu.gmall.feign.cart",
        "com.atguigu.gmall.feign.product",
        "com.atguigu.gmall.feign.user",
        "com.atguigu.gmall.feign.ware",
        "com.atguigu.gmall.feign.pay"})
@MapperScan("com.atguigu.gmall.order.mapper")
@EnableFeignAuthHeaderInterceptor
@EnableAutoHandleException
@EnableTransactionManagement
@EnableAppDoubleThreadPool
@EnableRabbit
@Configuration
public class AppConfig {
}
