package com.demo.manage.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.ZoneId;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.demo.client"})
public class ManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
        ZoneId.of("Asia/Shanghai");
    }

}
