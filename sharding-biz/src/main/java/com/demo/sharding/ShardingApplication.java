package com.demo.sharding;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;

@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class}) //排除掉druid自动加载，不然会找不到datasource配置，因为sharding-jdbc配置路径与druid不一致
public class ShardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingApplication.class, args);
        ZoneId.of("Asia/Shanghai");
    }

}
