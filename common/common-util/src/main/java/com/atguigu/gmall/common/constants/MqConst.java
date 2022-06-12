package com.atguigu.gmall.common.constants;

public class MqConst {

    public static final String ORDER_EVENT_EXCHANGE = "order-event-exchange";//订单交换机名
    public static final String RK_ORDER_TIMEOUT = "order.timeout";//交换机指向延迟队列的键
    public static final String ORDER_DELAY_QUEUE = "order-delay-queue";//延迟队列
    public static final String RK_ORDER_CREATE = "order.create";//交换机指向死信队列的键
    public static final String ORDER_DEAD_QUEUE = "order-dead-queue";//死信队列

    public static final String RK_ORDER_PAYED = "order.payed";
    public static final String ORDER_PAYED_QUEUE = "order-payed-queue";
}
