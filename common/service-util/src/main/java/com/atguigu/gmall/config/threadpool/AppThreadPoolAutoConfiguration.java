package com.atguigu.gmall.config.threadpool;




import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;;

/**
 * 线程池的自动配置类
 */
@Slf4j
@EnableConfigurationProperties(AppThreadPoolProperties.class)
@Configuration
public class AppThreadPoolAutoConfiguration {

//    @Autowired
//    ThreadPoolExecutor corePool;

    @Primary
    @Bean
    public ThreadPoolExecutor corePool(AppThreadPoolProperties poolProperties,
                                       @Value("${spring.application.name:defaultApp}") String appName){
        log.info("业务核心线程池准备完成");
        return new ThreadPoolExecutor(poolProperties.getCorePoolSize(),
                poolProperties.getMaximumPoolSize(),
                poolProperties.getKeepAliveTime(),
                poolProperties.getUnit(),
                new LinkedBlockingQueue<>(poolProperties.getQueueSize()),
                new AppThreadFactory(appName+"-core"),
                poolProperties.getRejectHandler());
    }

    @Bean
    public ThreadPoolExecutor otherPool(AppThreadPoolProperties poolProperties,
                                       @Value("${spring.application.name:defaultApp}") String appName){
        log.info("业务非核心线程池准备完成");
        return new ThreadPoolExecutor(poolProperties.getCorePoolSize()/2,
                poolProperties.getMaximumPoolSize()/2,
                poolProperties.getKeepAliveTime(),
                poolProperties.getUnit(),
                new LinkedBlockingQueue<>(poolProperties.getQueueSize()/2),
                new AppThreadFactory(appName+"-other"),
                poolProperties.getRejectHandler());
    }

    //自定义线程工厂
    class AppThreadFactory implements ThreadFactory{
        private String appName;
        private AtomicInteger count = new AtomicInteger(1);
        public AppThreadFactory(String appName){
            this.appName = appName;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            // [service-product]-core-1
            thread.setName("["+appName+"]" + count.getAndIncrement());
            return thread;
        }
    }
}
