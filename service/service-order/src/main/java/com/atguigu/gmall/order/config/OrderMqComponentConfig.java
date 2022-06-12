package com.atguigu.gmall.order.config;

import com.atguigu.gmall.common.constants.MqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 给容器中放好
 * 1. 交换机,队列,绑定关系,第一次使用如果还没有,就会自动给RabbitMq创建这些
 */
@Configuration
public class OrderMqComponentConfig {


    /**
     * 订单总交换机
     * @return
     */
    @Bean
    public Exchange orderEventExchange(){

        /**
         * String name,
         * boolean durable,
         * boolean autoDelete,
         * Map<String, Object> arguments
         */
        return new TopicExchange(MqConst.ORDER_EVENT_EXCHANGE,true,false,null);
    }


    /**
     * 订单关单-延迟队列
     * @return
     */
    @Bean
    public Queue orderdelayqueue(){
        /**
         * String name,
         * boolean durable,   持久化
         * boolean exclusive, 排他
         * boolean autoDelete, 自动删除
         * Map<String, Object> arguments
         */
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange",MqConst.ORDER_EVENT_EXCHANGE);
        params.put("x-dead-letter-routing-key",MqConst.RK_ORDER_TIMEOUT);
        params.put("x-message-ttl",60000);

        return new Queue(MqConst.ORDER_DELAY_QUEUE,
                true,
                false,
                false,
                params);
    }

    /**
     * 订单交换机-延迟队列  的绑定关系
     * @return
     */
    @Bean
    public Binding orderCreateBinding(){

        /**
         * String destination,                  目的地
         * DestinationType destinationType,     目的地类型
         * String exchange,                     交换机
         * String routingKey,                   路由键
         * Map<String, Object> arguments        参数
         *
         * 交换机 和 指定类型 的 目的地 使用 路由键 进行绑定
         * 订单交换机 和 延迟队列 使用 order.create 进行绑定
         */
        return new Binding(MqConst.ORDER_DELAY_QUEUE,
                Binding.DestinationType.QUEUE,
                MqConst.ORDER_EVENT_EXCHANGE,
                MqConst.RK_ORDER_CREATE,
                null);
    }


    /**
     * 订单的死信队列，关单服务需要监听这个队列，进行关单
     * @return
     */
    @Bean
    public Queue orderDeadQueue(){

        /**
         * String name,
         * boolean durable, boolean exclusive, boolean autoDelete,
         *                        @Nullable Map<String, Object> arguments
         */
        return new Queue(MqConst.ORDER_DEAD_QUEUE,true,false,false);
    }

    /**
     * 订单交换机-死信队列 的绑定
     * @return
     */
    @Bean
    public Binding orderDeadBinding(){

        /**
         * String destination,                  目的地
         * DestinationType destinationType,     目的地类型
         * String exchange,                     交换机
         * String routingKey,                   路由键
         * Map<String, Object> arguments        参数
         *
         * 交换机 和 指定类型 的 目的地 使用 路由键 进行绑定
         * 订单交换机 和 死信队列 使用 order.timeout 进行绑定
         */
        return new Binding(MqConst.ORDER_DEAD_QUEUE,
                Binding.DestinationType.QUEUE,
                MqConst.ORDER_EVENT_EXCHANGE,
                MqConst.RK_ORDER_TIMEOUT,
                null);
    }

    /**
     * 支付成功单队列
     * @return
     */
    @Bean
    public Queue payedQueue(){
        return new Queue(MqConst.ORDER_PAYED_QUEUE,true,false,false);
    }

    @Bean
    public Binding orderPayedBinding(){

        return new Binding(MqConst.ORDER_PAYED_QUEUE,
                Binding.DestinationType.QUEUE,
                MqConst.ORDER_EVENT_EXCHANGE,
                MqConst.RK_ORDER_PAYED,
                null);
    }

}
