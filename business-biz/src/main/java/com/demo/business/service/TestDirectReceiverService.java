package com.demo.business.service;

import com.alibaba.fastjson.JSON;
import com.demo.business.entity.TestMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@ConditionalOnProperty(
        prefix = "spring.rabbitmq",
        name = {"enabled"},
        havingValue = "true"
)
public class TestDirectReceiverService {

    /**
     * Direct消息消费者，同时手动确认模式
     */
    @RabbitListener(queues="${spring.rabbitmq.receivers.test-direct-queue.queueName}"
            , ackMode = "${spring.rabbitmq.receivers.test-direct-queue.acknowledgeMode}")
    public void receive(TestMessage testMessage, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("handleDirectMessage:{}", JSON.toJSONString(testMessage));
        channel.basicAck(tag,true);
    }

}
