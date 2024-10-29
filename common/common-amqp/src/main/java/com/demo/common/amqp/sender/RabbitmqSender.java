package com.demo.common.amqp.sender;

import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 封装生产者
 */
public class RabbitmqSender {

    private RabbitTemplate rabbitTemplate;

    public RabbitmqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 发送只有交换机和数据的消息
     * @param object 消息
     * @param exchange 交换机
     */
    public void send(Object object, String exchange) {
        send(object, exchange, "" );
    }

    /**
     * 发送带有routingKey的消息
     * @param object 消息
     * @param exchange 交换机
     * @param routingKey 路由key
     */
    public void send(Object object, String exchange, String routingKey) {
        send(object, null, exchange, routingKey, null);
    }

    /**
     * 发送带有headers的消息
     * @param object 消息
     * @param exchange 交换机
     * @param headers 头部信息
     */
    public void send(Object object, String exchange, Map<String, Object> headers) {
        send(object, null, exchange, null, headers);
    }

    /**
     * 根据入参发送消息
     * @param object 消息
     * @param delay 延迟时间
     * @param exchange 交换机
     * @param routingKey 路由key
     * @param headers 头部信息
     */
    public void send(Object object, Integer delay, String exchange, String routingKey, Map<String, Object> headers) {
        rabbitTemplate.convertAndSend(exchange, routingKey, object, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            if (Objects.nonNull(delay)) {
                messageProperties.setDelay(delay);
            }
            messageProperties.setMessageId(UUID.randomUUID().toString());
            if(headers!=null && headers.size()>0){
                for (String key : headers.keySet()){
                    messageProperties.setHeader(key, headers.get(key));
                }
            }
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        });
    }




}
