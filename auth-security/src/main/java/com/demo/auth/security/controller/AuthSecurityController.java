package com.demo.auth.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 */
@RestController
public class AuthSecurityController {

    @GetMapping("/demo")
    public String demo() {
        return"demo";
    }

}
