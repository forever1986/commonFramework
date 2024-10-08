package com.demo.auth.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 */
@RestController
public class DemoController {

    @GetMapping("/demo")
    public String demo() {
        return"demo";
    }

}
