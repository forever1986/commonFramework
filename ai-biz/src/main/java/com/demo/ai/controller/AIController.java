package com.demo.ai.controller;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.demo.ai.config.QwenConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@EnableConfigurationProperties(QwenConfigProperties.class)
@RestController
public class AIController {


    @Value("${spring.ai.qwen.api-key}")
    private String apiKey = "";

    @Autowired
    private QwenConfigProperties qwenConfigProperties;

    @GetMapping("/ai/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "你是谁？") String message) throws NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(qwenConfigProperties.getSysteMessage())
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(message)
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model(qwenConfigProperties.getModel())
                .temperature(qwenConfigProperties.getTemperature())
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        GenerationResult result = gen.call(param);
        GenerationOutput output = result.getOutput();
        return output.getChoices().get(0).getMessage().getContent();
    }

}
