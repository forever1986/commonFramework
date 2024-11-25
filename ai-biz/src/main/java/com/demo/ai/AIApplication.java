package com.demo.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;

@SpringBootApplication
public class AIApplication {

    public static void main(String[] args) {
        SpringApplication.run(AIApplication.class, args);
        ZoneId.of("Asia/Shanghai");
    }

}
