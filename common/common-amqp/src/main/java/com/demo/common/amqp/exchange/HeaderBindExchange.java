package com.demo.common.amqp.exchange;

import com.demo.common.amqp.receiver.QueueInfo;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.util.ArrayList;
import java.util.List;

/**
 * Header交换机绑定队列
 */
public class HeaderBindExchange implements BindExchange{

    @Override
    public List<Binding> bind(RabbitAdmin rabbitAdmin, Queue queue, QueueInfo queueInfo) {
        List<Binding> bindings = new ArrayList<>();
        if(queueInfo.getHeaders()!=null&&queueInfo.getHeaders().size()>0){
            Binding binding = null;
            if(queueInfo.isMatchAll()){
                binding = BindingBuilder.bind(queue).to(new HeadersExchange(queueInfo.getExchange())).whereAll(queueInfo.getHeaders()).match();
            }else{
                binding = BindingBuilder.bind(queue).to(new HeadersExchange(queueInfo.getExchange())).whereAny(queueInfo.getHeaders()).match();
            }
            rabbitAdmin.declareBinding(binding);
            bindings.add(binding);
        }
        return bindings;
    }

}
