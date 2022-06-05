package com.atguigu.gmall.web;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *
 * 页面渲染
 * 1.引入 thymeleaf
 * 2.编写 Controller, 处理请求并 return 页面,并给页面放入一些属性
 * 3.编写页面渲染逻辑, ${}取值遍历等操作
 *
 */

@SpringCloudApplication
public class WebAllApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAllApplication.class,args);
    }

}
