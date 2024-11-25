package com.demo.ai.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "spring.ai.qwen.chat.options"
)
@Data
@NoArgsConstructor
public class QwenConfigProperties {

    private String systeMessage = "";
    private String model = "";
    private Float temperature = 0.9f;

}
