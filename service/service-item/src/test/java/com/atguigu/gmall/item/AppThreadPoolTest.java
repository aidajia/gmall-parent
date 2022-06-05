package com.atguigu.gmall.item;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * 1. new Thread
 *
 * 2. 异步(CompletableFuture)+线程池(ThreadPoolExecutor)
 * CompletableFuture
 *
 * 使用线程池:
 * 1.准备一个自定义线程池
 * 2.CompletableFuture 给线程池中提交任务
 * 3.对提交的任务进行编排、组合、容错处理
 *
 */
@SpringBootTest
public class AppThreadPoolTest {

    @Autowired
    ThreadPoolExecutor poolExecutor;


    /**
     * then系列进行任务编排
     * 1、thenRun：  传入 Runnable 启动一个无返回值的异步任务，
     *      thenRun
     *      thenRunAsync
     *      thenRunAsync(带线程池)
     * 2、thenAccept:   传入 Consumer  void accept(T t); 接参数，但是也无返回值
     *      thenAccept
     *      thenAcceptAsync
     *      thenAcceptAsync(带线程池)
     * 3、thenApply: 传入  Function:  R apply(T t);  而且有返回值
     *
     */
    @Test
    public void thenTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ": 正在计算");
            int i = 1 + 1;
            return i;
        }, poolExecutor).thenApplyAsync((result)->{
            System.out.println(Thread.currentThread().getName() + ": 正在转换");
            return result + 10;
        },poolExecutor).thenApplyAsync((result)->{
            System.out.println(Thread.currentThread().getName() + ": 正在转换");
            return result + "A";
        });
        String s = future.get();
        System.out.println("结果："+s);
    }
}
