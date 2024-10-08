package com.demo.manage.biz.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
//@ConfigurationProperties(prefix = "mqtt")
public class NacosValueConstant {

    @Value("${my.project.name}")
    private String value;

    @Value("${lin.test}")
    private String test;


}
