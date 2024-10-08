package com.demo.auth.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;

@SpringBootApplication
public class AuthSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthSecurityApplication.class, args);
        ZoneId.of("Asia/Shanghai");
    }

}

