package com.demo.manage.biz.controller;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 集成JUnit测试，这个测试会启动spring-boot，同时类注入也是真实的
 * 1.意味着测试要启动spring-boot
 * 2.所有的数据库、网络等连接也是真实的
 * 更适合完整的自动化测试
 */
@SpringBootTest
@RunWith(SpringRunner.class) //spring-boot-2.6版本及之后，就不需要该注解
@ActiveProfiles("test") //配置测试环境
public class DemoControllerTest {

    @Autowired
    private DemoController demoController;

    @Test
    public void echo() {
        Assertions.assertEquals("你好",demoController.echo("你好"));
    }

}
