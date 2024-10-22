package com.demo.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class TicketOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketOrderApplication.class, args);
        log.info("订票服务服务已经启动...");
    }
}
