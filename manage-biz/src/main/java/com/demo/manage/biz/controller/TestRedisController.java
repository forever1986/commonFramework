package com.demo.manage.biz.controller;

import com.demo.manage.biz.service.TestRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestRedisController {

    @Autowired
    TestRedisService testRedisService;

    @GetMapping("/redistestobject")
    public void redisTestObject() throws InterruptedException {
        testRedisService.testObject();
    }

    @GetMapping("/redistestbyte")
    public void redisTestByte() throws IOException {
        testRedisService.testByte();
    }
}
