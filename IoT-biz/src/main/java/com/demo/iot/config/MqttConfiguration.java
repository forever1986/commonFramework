package com.demo.iot.config;

import com.demo.iot.properties.MqttProperties;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfiguration {

    @Autowired
    private MqttProperties properties;

    /**
     * 创建MqttPahoClientFactory，供消费者和生产者使用
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(properties.getUrls());
        options.setUserName(properties.getUsername());
        options.setCleanSession(properties.isCleanSession());
        options.setPassword(properties.getPassword().toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * 配置消费者
     * @return
     */
    @Bean
    @ConditionalOnProperty(
            prefix = "mqtt.receiver",
            name = {"enabled"},
            havingValue = "true"
    )
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "mqtt.receiver",
            name = {"enabled"},
            havingValue = "true"
    )
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(properties.getReceiver().getClient_id(),
                        mqttClientFactory(), properties.getReceiver().getTopic());
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(properties.getReceiver().getQos());
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    @ConditionalOnProperty(
            prefix = "mqtt.receiver",
            name = {"enabled"},
            havingValue = "true"
    )
    public MessageHandler handler() {
        return message -> {
            String payload = (String) message.getPayload();
            System.out.println("Received message: " + payload);
        };
    }

    /**
     * 配置生产者
     * @return
     */
    @Bean
    @ConditionalOnProperty(
            prefix = "mqtt.producer",
            name = {"enabled"},
            havingValue = "true"
    )
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "mqtt.producer",
            name = {"enabled"},
            havingValue = "true"
    )
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(properties.getProducer().getClient_id(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(properties.getProducer().getTopic());
        return messageHandler;
    }

}
