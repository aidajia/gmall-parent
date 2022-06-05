package com.atguigu.gmall.starter.cache.aop;



import com.atguigu.gmall.starter.constant.RedisConst;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * aop就是filter思想:抽取公共逻辑
 * 使用切面完成缓存的自动拦截逻辑
 *
 * 1. 导入 aop-starter
 * 2. @EnableAspectJAutoProxy
 * 3. 编写切面
 * 4. 切入点表达式
 *
 *      com.atguigu.gmall.**.SkuDetailServiceImpl.getSkuDetail(Long)
 *
 */
@Component //切面也必须放在springboot容器中才能起作用
@Aspect
public class CacheAspect {


    @Autowired
    CacheHelper cacheHelper;

    /**
     * 拦截方法：通知方法；
     *
     * 普通通知：只是通知，不能干扰目标方法执行
     * 前置通知：    前置通知 - 目标方法
     * 后置通知：    目标方法 - 目标方法以后 【后置通知】 finally
     * 返回通知：    目标方法 - 正常返回以后 【返回通知】 try
     * 异常通知：    目标方法 - 发生异常以后 【异常通知】 catch
     * 通知：目标方法到达指定阶段以后调用通知方法，通知方法只能感知，不能干扰
     *
     *  正常执行：  前置通知 --- 【目标方法】 --- 返回通知 --- 后置通知
     *  异常执行：  前置通知 --- 【目标方法】 --- 异常通知 --- 后置通知
     *
     *
     *
     * 环绕通知：编程式通知；干扰目标方法执行。决定是否执行目标方法
     * 1、方法必须返回Object。代理目标方法执行，返回数据
     * 2、参数必须是 ProceedingJoinPoint；
     *
     */
    @Around(value = "@annotation(com.atguigu.gmall.starter.cache.aop.annotation.Cache )")
    public Object around(ProceedingJoinPoint joinPoint){
        //1. 获取目标方法参数
        Object[] args = joinPoint.getArgs();
        Object result = null;
        try {
            //动态计算表达式
            String cacheKey = cacheHelper.evaluteExpression(joinPoint);
            //1.先查询缓存中有没有这个数据, 就是当前方法的返回值类型数据
            Object obj = cacheHelper.getCacheData(cacheKey,joinPoint);
            if(obj == null){
                //2.缓存中没有,准备回顾,准备回源锁
                String lockKey = RedisConst.LOCK_PREFIX+cacheKey;

                //判断布隆是否启用
                String bloomName = cacheHelper.determinBloom(joinPoint);
                if(StringUtils.isEmpty(bloomName)){
                    //不启用布隆,直接调用目标方法.并且要加锁
                    boolean tryLock = cacheHelper.tryLock(lockKey);
                        if(tryLock){
                            //2.1.1 加锁成功,回顾.放行目标方法进行查询
                            result = joinPoint.proceed(args);// 就是数据库查数据 执行目标方法. Object object = method.invoke(args);
                            //2.1.2 把数据存起来
                            cacheHelper.saveData(cacheKey,result);
                            //解锁
                            cacheHelper.unlock(lockKey);
                            //2.1.3 返回目标方法的数据
                            return result;
                        }

                        //2.2 没加锁成功, 睡1s再查缓存,返回数据
                        Thread.sleep(1000);
                        obj = cacheHelper.getCacheData(cacheKey,joinPoint);
                        return obj;
                }else{
                    //启用布隆,要自动用指定的布隆进行判断
                    boolean bloomcontains = cacheHelper.bloomTest(bloomName,joinPoint);
                    if(bloomcontains){
                        //布隆说有, 尝试加锁
                        boolean tryLock = cacheHelper.tryLock(lockKey);
                        if(tryLock){
                            //2.1.1 加锁成功,回顾.放行目标方法进行查询
                            result = joinPoint.proceed(args);// 就是数据库查数据 执行目标方法. Object object = method.invoke(args);
                            //2.1.2 把数据存起来
                            cacheHelper.saveData(cacheKey,result);
                            //解锁
                            cacheHelper.unlock(lockKey);
                            //2.1.3 返回目标方法的数据
                            return result;
                        }

                        //2.2 没加锁成功, 睡1s再查缓存,返回数据
                        Thread.sleep(1000);
                        obj = cacheHelper.getCacheData(cacheKey,joinPoint);
                        return obj;
                    }else {
                        //布隆说没有
                        return null;
                    }
                }


            }

            //3. 缓存中有
            return obj;
            //返回通知

        } catch (Throwable throwable) {
            //异常通知


        }finally {
            //后置通知


        }


        //3.将目标方法的结果进行返回
        return result;
    }

}
