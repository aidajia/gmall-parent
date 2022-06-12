package com.atguigu.gmall.pay.config;

import com.atguigu.gmall.annotation.EnableFeignAuthHeaderInterceptor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = {"com.atguigu.gmall.feign.order"})
@EnableFeignAuthHeaderInterceptor
@EnableAutoConfiguration
@Configuration
public class AppConfig {
}
