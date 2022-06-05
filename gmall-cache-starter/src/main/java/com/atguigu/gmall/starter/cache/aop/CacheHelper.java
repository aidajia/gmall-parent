package com.atguigu.gmall.starter.cache.aop;


import com.atguigu.gmall.starter.cache.aop.annotation.Cache;
import com.atguigu.gmall.starter.cache.service.CacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CacheHelper {

    @Autowired
    CacheService cacheService;
    SpelExpressionParser parser = new SpelExpressionParser();

    @Qualifier("skuIdBloom")
    @Autowired
    RBloomFilter<Object> skuIdBloom;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    Map<String,RBloomFilter<Object>> bloomMap;

    @Autowired
    RedisTemplate redisTemplate;

    //定时任务线程池
    ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(5);

    public Object getCacheData(String cacheKey, ProceedingJoinPoint joinPoint) {
        //拿目标方法的返回值类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取目标方法
        Method method = signature.getMethod();
        Type type = method.getGenericReturnType();//带泛型的返回类

        Object data = cacheService.getCacheData(cacheKey, new TypeReference<Object>() {
            @Override
            public Type getType() {
                return type;
            }
        });

        System.out.println("从缓存拿到的数据: "+data);
        return data;
    }

//    public boolean bloomTest(Object arg) {
//
//        return skuIdBloom.contains(arg);
//    }

    public boolean tryLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean tryLock = lock.tryLock();
        return tryLock;
    }

    public void saveData(String cacheKey, Object result) {
        cacheService.save(cacheKey,result);
    }

    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.isLocked()){
                lock.unlock();
            }
        } catch (Exception e) {

        }
    }

    public String evaluteExpression(ProceedingJoinPoint joinPoint) {
        //1.拿到目标方法上标注的cacheKey的值
        Cache cache = getMethodCacheAnnotation(joinPoint);

        //3.拿到表达式 sku:detail:#{#args[0]}
        String cacheKeyExpression = StringUtils.isEmpty(cache.cacheKey()) ? cache.value() : cache.cacheKey();
        String expValue = getExpressionValueString(joinPoint, cacheKeyExpression,String.class);

        return expValue; //算出的表达式值
    }

    private <T> T getExpressionValueString(ProceedingJoinPoint joinPoint, String cacheKeyExpression,Class<T> clz) {
        //4.计算表达式
        Expression expression = parser.parseExpression(cacheKeyExpression, ParserContext.TEMPLATE_EXPRESSION);
        //准备上下文信息
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("args",joinPoint.getArgs());
        context.setVariable("nowTime",System.currentTimeMillis());
        //未来各种东西自己放

        T value = expression.getValue(context, clz);
        return value;
    }

    /**
     * 获取方法上的@Cache注解
     * @param joinPoint
     * @return
     */
    private Cache getMethodCacheAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        //2.拿到目标方法上的注解
        return AnnotationUtils.findAnnotation(method, Cache.class);
    }

    /**
     * 判断布隆是否启用
     * @param joinPoint
     * @return
     */
    public String determinBloom(ProceedingJoinPoint joinPoint) {
        Cache cache = getMethodCacheAnnotation(joinPoint);

        String bloomName = cache.bloomName();

        return  bloomName;
    }

    /**
     * 判断 bloomName 指定的 布隆过滤器中是否 有指定的值
     * @param bloomName
     * @param joinPoint
     * @return
     */
    public boolean bloomTest(String bloomName, ProceedingJoinPoint joinPoint) {
        Cache cache = getMethodCacheAnnotation(joinPoint);

        String bloomValueExp = cache.bloomValue();

        //1. 计算出布隆过滤器需要判断的值
        Object value = getExpressionValueString(joinPoint, bloomValueExp,Object.class);

        log.info("布隆准备判定的值: {}  类型: {}",value,value.getClass());

        //2. 动态取出指定布隆
        RBloomFilter<Object> bloomFilter = bloomMap.get(bloomName);

        return bloomFilter.contains(value);
    }

    public void deleteCache(String key) {
        //一定做
        redisTemplate.delete(key); //90%

        //立即提交给线程池, 不能保证它一定运行
        threadPool.schedule(()->{
            redisTemplate.delete(key);
        },10, TimeUnit.SECONDS); //99%

        // sava数据有过期时间  100% : 保证脏数据不会在系统中永久保存
    }
}
