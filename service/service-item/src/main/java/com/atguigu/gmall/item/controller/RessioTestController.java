package com.atguigu.gmall.item.controller;


import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@RestController
public class RessioTestController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

//    ReentrantLock lock = new ReentrantLock();

    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/redis/incr")
    public String incrWithRedisson(){
        RLock lock = redissonClient.getLock("lock-abcd");

        try {
            lock.lock();
            //1.获取原值
            String hellocount = stringRedisTemplate.opsForValue().get("hellocount");
            int count = Integer.parseInt(hellocount);

            //2.计算新值
            count++;

            //3.修改新值
            stringRedisTemplate.opsForValue().set("hellocount",count+"");
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return "ok";
    }

    @GetMapping("/redis/incr/aaa")
    public String incr(){
        System.out.println("处理请求...");

        //第一次抢锁: false
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", "1", 10, TimeUnit.SECONDS);

        //只要是false就进入while死循环
        while (!lock){
            lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", "1", 10, TimeUnit.SECONDS);
        }

        //1.获取原值
        String hellocount = stringRedisTemplate.opsForValue().get("hellocount");
        int count = Integer.parseInt(hellocount);

        //2.计算新值
        count++;

        //3.修改新值
        stringRedisTemplate.opsForValue().set("hellocount",count+"");

        //4.删除锁
        stringRedisTemplate.delete("lock");

        return "ok";
    }

}
