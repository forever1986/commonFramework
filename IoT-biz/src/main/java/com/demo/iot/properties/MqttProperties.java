package com.demo.iot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 获取MQ中的消息消费者配置
 */
@ConfigurationProperties(prefix = "mqtt")
@Data
public class MqttProperties {

    private String [] urls;
    private String username;
    private String password;
    private boolean cleanSession = false;

    private ReceiverInfo receiver;

    private ProducerInfo producer;

}
