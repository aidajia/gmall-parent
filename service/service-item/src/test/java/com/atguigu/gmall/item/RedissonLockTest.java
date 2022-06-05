package com.atguigu.gmall.item;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedissonLockTest {

    @Autowired
    RedissonClient redissonClient;

    /**
     * 可重入锁
     * lock.lock();
     * 1.防死锁: 默认锁30s
     * 2.锁续期: 只要业务超长, 就会自动续期. 每隔1/3的锁时间, 就会自动续满期
     *      小心: 只要我们传了锁的自动释放时间, 就会取消自动续期功能
     * 3.锁原子性保证
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/lock/hello")
    public String reentrantLock() throws InterruptedException {
        //1.得到锁
        RLock lock = redissonClient.getLock("hello-lock");

        //2.加锁
        lock.lock();//阻塞式加锁

        System.out.println("哈哈");
        Thread.sleep(1000*60);
        //3.解锁
        lock.unlock();

        return "ok";

    }

}
