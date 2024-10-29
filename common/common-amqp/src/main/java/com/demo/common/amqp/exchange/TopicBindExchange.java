package com.demo.common.amqp.exchange;

import com.demo.common.amqp.receiver.QueueInfo;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.util.ArrayList;
import java.util.List;

/**
 * Topic交换机绑定队列
 */
public class TopicBindExchange implements BindExchange{

    @Override
    public List<Binding> bind(RabbitAdmin rabbitAdmin, Queue queue, QueueInfo queueInfo) {
        List<Binding> bindings = new ArrayList<>();
        if(queueInfo.getRoutingKey()!=null && queueInfo.getRoutingKey().length > 0){
            for(String rountingKey : queueInfo.getRoutingKey()){
                Binding binding = BindingBuilder.bind(queue).to(new TopicExchange(queueInfo.getExchange())).with(rountingKey);
                rabbitAdmin.declareBinding(binding);
                bindings.add(binding);
            }
        }
        return bindings;
    }

}
