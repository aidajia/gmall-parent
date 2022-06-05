package com.atguigu.gmall.starter.annotation;


import com.atguigu.gmall.starter.cache.service.impl.CacheServiceImpl;
import com.atguigu.gmall.starter.redisson.AppRedissonAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import({AppRedissonAutoConfiguration.class, CacheServiceImpl.class})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableAppRedissonAndCache {
}
