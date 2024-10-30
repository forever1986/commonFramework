package com.demo.common.amqp.config;

import com.demo.common.amqp.exchange.ExchangeType;
import com.demo.common.amqp.receiver.QueueInfo;
import com.demo.common.amqp.receiver.RabbitmqProperties;
import com.demo.common.amqp.sender.RabbitmqSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableRabbit
@EnableConfigurationProperties(RabbitmqProperties.class)
@Slf4j
public class RabbitmqConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final RabbitmqProperties rabbitmqProperties;

    public RabbitmqConfig(RabbitmqProperties rabbitmqProperties) {
        this.rabbitmqProperties = rabbitmqProperties;
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.rabbitmq",
            name = {"enabled"},
            havingValue = "true"
    )
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * mq消息序列化类
     * @return
     */
    @Bean
    @ConditionalOnProperty(
            prefix = "spring.rabbitmq",
            name = {"enabled"},
            havingValue = "true"
    )
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setCreateMessageIds(true);
        return jackson2JsonMessageConverter;
    }

    /**
     * 封装消息发送
     * @return
     */
    @Bean
    public RabbitmqSender rabbitmqSender() {
        return new RabbitmqSender(rabbitTemplate);
    }

    /**
     * 批量注册队列
     */
    @Bean
    @ConditionalOnProperty(
            prefix = "spring.rabbitmq",
            name = {"enabled"},
            havingValue = "true"
    )
    public List<Queue> rabbitQueues(RabbitAdmin rabbitAdmin) {
        List<Queue> queues = new ArrayList<>();

        Map<String, QueueInfo> topics = rabbitmqProperties.getReceivers();

        for (QueueInfo queueInfo : topics.values()) {
            String queueName = queueInfo.getQueueName();
            Queue queue = new Queue(queueName);
            queue.setShouldDeclare(true);
            queues.add(queue);
            rabbitAdmin.declareQueue(queue);
            log.info("成功注册queue=[{}]", queueName);
        }
        return queues;
    }

    /**
     * 批量绑定交换机与队列
     */
    @Bean
    @ConditionalOnProperty(
            prefix = "spring.rabbitmq",
            name = {"enable"},
            havingValue = "true"
    )
    public List<Binding> bindings(RabbitAdmin rabbitAdmin, List<Queue> rabbitQueues) {
        List<Binding> bindings = new ArrayList<>();
        Map<String, QueueInfo> topics = rabbitmqProperties.getReceivers();
        for (String key : topics.keySet()) {
            QueueInfo queueInfo = topics.get(key);
            ExchangeType exchangeType = ExchangeType.valueOf(queueInfo.getExchangeType());
            Queue curQueue = null;
            for(Queue queue : rabbitQueues){
                if(queue.getName().equals(queueInfo.getQueueName())){
                    curQueue = queue;
                    break;
                }
            }
            bindings.addAll(exchangeType.getExchange().bind(rabbitAdmin,curQueue,queueInfo));
            log.info("队列=[{}]绑定到交换机=[{}]", curQueue.getName(), queueInfo.getExchange());
        }
        return bindings;
    }


}
