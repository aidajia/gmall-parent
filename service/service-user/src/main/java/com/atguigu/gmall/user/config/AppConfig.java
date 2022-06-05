package com.atguigu.gmall.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(basePackages = "com.atguigu.gmall.user.mapper")
@EnableTransactionManagement
@Configuration
public class AppConfig {
}
