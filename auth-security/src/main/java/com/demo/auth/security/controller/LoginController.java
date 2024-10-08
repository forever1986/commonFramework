package com.demo.auth.security.controller;

import com.demo.auth.security.service.LoginService;
import com.demo.auth.security.entity.TUser;
import com.demo.auth.security.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录和登出配置
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /*
     * 登录接口
     * */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody TUser user) {
        // 认证通过，返回给前端jjwt
        return loginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        // 认证通过，返回给前端jjwt
        return loginService.logout();
    }

}
