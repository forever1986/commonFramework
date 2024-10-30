package com.demo.business.service;

import com.alibaba.fastjson.JSON;
import com.demo.business.entity.TestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(
        prefix = "spring.rabbitmq",
        name = {"enabled"},
        havingValue = "true"
)
public class TestTopicReceiverService {

    /**
     * Topic消息消费者，同时自动确认模式
     */
    @RabbitListener(queues="${spring.rabbitmq.receivers.test-topic-queue.queueName}")
    public void receive(TestMessage testMessage) {
        log.info("handleDirectMessage:{}", JSON.toJSONString(testMessage));
    }

}
