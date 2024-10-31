package com.demo.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqttController {

    @Autowired
    private MessageChannel mqttOutboundChannel;


    @GetMapping("/sendmqtt")
    public String send() {
        String message = "{\"id\": 6 , \"name\": \"test6\"}";
        mqttOutboundChannel.send(MessageBuilder.withPayload(message)
                .build());
        return "ok";
    }
}
