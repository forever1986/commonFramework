package com.demo.common.amqp.receiver;

import lombok.Data;

import java.util.Map;

/**
 * 消息消费者的配置类
 */
@Data
public class QueueInfo {
    private String queueName;
    private String exchange;
    private String exchangeType;
    private String acknowledgeMode;
    private String[] routingKey;
    private boolean matchAll;
    private Map<String, Object> headers;

}
