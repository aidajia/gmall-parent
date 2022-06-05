package com.atguigu.gmall.starter.cache.aop.annotation;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {

    //自定义属性
    String value() default ""; //代表key, 缓存用的key

    @AliasFor("value")
    String cacheKey() default "";

    //传入布隆过滤器的名字
    String bloomName() default "";

    //如果启用布隆过滤器, 布隆过滤器判断用的值
    String bloomValue() default "";

}
