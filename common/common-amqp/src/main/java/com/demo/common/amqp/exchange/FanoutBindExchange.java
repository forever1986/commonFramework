package com.demo.common.amqp.exchange;

import com.demo.common.amqp.receiver.QueueInfo;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.util.ArrayList;
import java.util.List;

/**
 * Fanout交换机绑定队列
 */
public class FanoutBindExchange implements BindExchange{

    @Override
    public List<Binding> bind(RabbitAdmin rabbitAdmin, Queue queue, QueueInfo queueInfo) {
        List<Binding> bindings = new ArrayList<>();
        Binding binding = BindingBuilder.bind(queue).to(new FanoutExchange(queueInfo.getExchange()));
        rabbitAdmin.declareBinding(binding);
        bindings.add(binding);
        return bindings;
    }

}
