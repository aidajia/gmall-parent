package com.atguigu.gmall.starter.annotation;



import com.atguigu.gmall.starter.cache.aop.CacheAspect;
import com.atguigu.gmall.starter.cache.aop.CacheHelper;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import({
        CacheAspect.class,
        CacheHelper.class
})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableAutoCache {
}
