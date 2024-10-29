package com.demo.common.amqp.exchange;

import com.demo.common.amqp.receiver.QueueInfo;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.util.List;

/**
 * 绑定交换机与队列接口，具体看不同交换机实现方式
 */
public interface BindExchange {
    public List<Binding> bind(RabbitAdmin rabbitAdmin, Queue queue, QueueInfo queueInfo);
}
