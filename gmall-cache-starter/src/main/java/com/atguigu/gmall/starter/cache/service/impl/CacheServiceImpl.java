package com.atguigu.gmall.starter.cache.service.impl;


import com.atguigu.gmall.starter.cache.service.CacheService;
import com.atguigu.gmall.starter.utils.JSONS;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    StringRedisTemplate redisTemplate;
//    @Override
//    public List<CategoryAndChildTo> getCategorys() {
//        //1.远程查询redis的categorys数据
//        String categorys = redisTemplate.opsForValue().get("categorys");
//
//        //2.redis没有缓存这个key的数据
//        if (StringUtils.isEmpty(categorys)){
//            return null;
//        }
//
//        //3.redis有数据 [反序列化]
//        //对象转流(字符流、字节流): 序列化
//        //流转对象:                 反序列化
//        List<CategoryAndChildTo> data = JSONS.strToCategoryObj(categorys);
//        return data;
//    }
//
//    @Override
//    public void saveCategoryData(List<CategoryAndChildTo> childs) {
//        String toStr = JSONS.toStr(childs);
//        redisTemplate.opsForValue().set("categorys",toStr);
//    }

    @Override
    public <T extends Object> T getCacheData(String key, TypeReference<T> typeReference) {
        //1.获取redis指定key数据
        String json = redisTemplate.opsForValue().get(key);

        //2.判断
        if(!StringUtils.isEmpty(json)){
            if("no".equals(json)){
                T t = JSONS.nullInstance(typeReference);
                return (T)new Object();
            }
            //3.转换成指定的格式
            T t = JSONS.strToObj(json,typeReference);
            return t;
        }

        //4.缓存中真没有, 连查都没查过
        return null;//只要返回null就调用数据库逻辑
    }

    @Override
    public void save(String key, Object data) {
        if(data == null){
            //数据库是 null long timeout, TimeUnit unit //
            redisTemplate.opsForValue().set(key,"no",30, TimeUnit.MINUTES);
        }else {
            //为了防止同时过期,给每个过期时间加上随机值
            //89494641616.46151616
            Double v = Math.random() * 1000000000L;
            int mill = 1000 * 60 * 60 * 24 * 3 + v.intValue();

            redisTemplate.opsForValue().set(key,JSONS.toStr(data),mill,TimeUnit.MILLISECONDS);
        }
    }
}
