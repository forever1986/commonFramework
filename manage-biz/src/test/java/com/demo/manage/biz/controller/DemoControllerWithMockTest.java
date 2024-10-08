package com.demo.manage.biz.controller;

import com.demo.manage.biz.service.DemoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

/**
 * 使用MockitoJUnitRunner，则不会启动spring-boot，因此里面的除了测试类是真实实例化的，其中以@Mock注解的都是模拟的，并不会真实调用
 * 1.用于单元测试
 * 2.集成测试时，其它依赖还未read的情况下
 * 3.@InjectMocks：被测试类；@Mock：被测试类的依赖；@spy：监控模拟的类；
 */
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test") //配置测试环境
public class DemoControllerWithMockTest {

    @InjectMocks //真正测试类
    private DemoController demoController;

    @Mock //模拟的类，不会实例化真实的demoServices，一般用于数据库、网络调用等情况
    private DemoService demoService;

    @Before //在测试之前初始化数据或者模拟类
    public void init(){
        //定义一下模拟demoService的调用，无论输入什么值，都返回“你好”
        Mockito.when(demoService.echo(Mockito.anyString())).thenReturn("你好");
    }

    @Test
    public void whenGetUser_thenReturnUser() throws Exception {
        Assertions.assertEquals("你好",demoService.echo("测试"));
    }

}
