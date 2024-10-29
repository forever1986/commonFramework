package com.demo.business.controller;

import com.demo.business.entity.TestMessage;
import com.demo.common.amqp.sender.RabbitmqSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息发送测试接口
 */
@RestController
@Slf4j
public class MQController {

    @Autowired
    private RabbitmqSender rabbitmqSender;

    @Value("${spring.rabbitmq.senders.test-direct}")
    private String test_direct;

    @Value("${spring.rabbitmq.senders.test-header}")
    private String test_header;

    @Value("${spring.rabbitmq.senders.test-topic}")
    private String test_topic;

    @Value("${spring.rabbitmq.senders.test-fanout}")
    private String test_fanout;

    @GetMapping("/senddirect")
    public void sendDirectMessage(String str){
        TestMessage testMessage = new TestMessage(1,str,new Timestamp((new Date()).getTime()));
        rabbitmqSender.send(testMessage, test_direct, "error");
    }


    @GetMapping("/sendheader")
    public void sendHeaderMessage(String str){
        TestMessage testMessage = new TestMessage(1,str,new Timestamp((new Date()).getTime()));
        Map<String, Object> headers = new HashMap<>();
        headers.put("type","forqueue1");
        rabbitmqSender.send(testMessage, test_header, headers);
    }

    @GetMapping("/sendtopic")
    public void sendTopicMessage(String str){
        TestMessage testMessage = new TestMessage(1,str,new Timestamp((new Date()).getTime()));
        rabbitmqSender.send(testMessage, test_topic, "good.add");
    }

    @GetMapping("/sendfanout")
    public void sendFanoutMessage(String str){
        TestMessage testMessage = new TestMessage(1,str,new Timestamp((new Date()).getTime()));
        rabbitmqSender.send(testMessage, test_fanout);
    }

}
