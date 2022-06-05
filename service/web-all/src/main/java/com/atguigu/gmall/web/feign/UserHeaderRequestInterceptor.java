package com.atguigu.gmall.web.feign;

import com.atguigu.gmall.web.controller.CartController;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * feign请求拦截器解决远程调用丢失请求头问题
 * 功能： 每次远程调用之前，把老请求的UserId和UserTempId继续设置到新请求头中，往下透传
 */
@Component
public class UserHeaderRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
//        System.out.println(Thread.currentThread());
//        System.out.println("哈哈...");
        //老请求拿来？
//        HttpServletRequest httpServletRequest = CartController.threadLocal.get();
        //1、得到当前请求（请求刚进来Tomcat接到的这个老请求）

        ServletRequestAttributes request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String userId = request.getRequest().getHeader("UserId");
        if(userId != null){
            template.header("UserId",userId);
        }

        String UserTempId = request.getRequest().getHeader("UserTempId");
        if(UserTempId != null){
            template.header("UserTempId",UserTempId);
        }



    }
}
