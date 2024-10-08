package com.demo.auth.authentication.controller;

import com.demo.auth.authentication.entity.TUser;
import com.demo.auth.authentication.service.LoginService;
import com.demo.auth.authentication.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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
    public ResponseResult login(Principal principal, @RequestBody TUser user) {
        // 认证通过，返回给前端jjwt
        return loginService.login(user);
    }

    @PostMapping("/loginout") //注意这里不能使用logout，因为会导致重定向到loginPage.html页面
    public ResponseResult loginout() {
        // 认证通过，返回给前端jjwt
        return loginService.loginout();
    }

}
