package com.atguigu.gmall.item;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

public class GuavaTest {


    @Test
    public void bloomTest(){

        /**
         * Funnel<? super T> funnel,
         * int expectedInsertions, //预估数据量
         * double fpp  误判率
         */
        //1.准备一个数据存储规则的通道
        Funnel<Integer> funnel = Funnels.integerFunnel();

        //2.创建一个布隆过滤器
        BloomFilter<Integer> bloomFilter = BloomFilter.create(funnel, 1000000, 0.00001);

        //3.给布隆添加过滤数据
        bloomFilter.put(88);
        bloomFilter.put(98);
        bloomFilter.put(100);

        System.out.println("布隆过滤器初始化完成: 并且保存了 88,98,100");
        //4.判断布隆是否有这些数据,说没有,铁定没有
        System.out.println("88: " + bloomFilter.mightContain(88));
        System.out.println("90: " + bloomFilter.mightContain(90));
        System.out.println("100: " + bloomFilter.mightContain(100));

    }

}
