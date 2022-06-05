package com.atguigu.gmall.starter;


import com.atguigu.gmall.starter.annotation.EnableAppRedissonAndCache;
import com.atguigu.gmall.starter.annotation.EnableAutoCache;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
@EnableAppRedissonAndCache //既能用到缓存组件, 还能用到redissonclient
@EnableAutoCache
public class CacheStarterAutoConfiguration {



}
