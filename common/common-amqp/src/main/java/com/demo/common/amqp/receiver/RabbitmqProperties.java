package com.demo.common.amqp.receiver;

import com.demo.common.amqp.receiver.QueueInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取MQ中的消息消费者配置
 */
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Data
public class RabbitmqProperties {

    private Map<String, QueueInfo> receivers = new HashMap<>();
}
