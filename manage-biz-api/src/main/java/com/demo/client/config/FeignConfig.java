package com.demo.client.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class FeignConfig {

    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL; // 输出完全的日志信息
    }

}
