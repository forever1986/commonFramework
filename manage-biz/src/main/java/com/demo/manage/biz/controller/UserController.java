package com.demo.manage.biz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.manage.biz.entity.TUser;
import com.demo.manage.biz.page.UserPageQuery;
import com.demo.manage.biz.service.UserService;
import com.demo.common.log.enums.ModuleTypeEnum;
import com.demo.common.log.aspect.SysLog;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @SysLog(module= ModuleTypeEnum.MANAGE, description="测试getUser")
    @ApiOperation(value = "测试数据库")
    @GetMapping("/getuser")
    public TUser getTUserByUsername(@RequestParam String username){
        return userService.getTUserByUsername(username);
    }


    @SysLog(module= ModuleTypeEnum.MANAGE, description="测试listUser")
    @ApiOperation(value = "测试数据库")
    @GetMapping("/listuser")
    public IPage<TUser> listUser(@RequestBody UserPageQuery userPageQuery){
        return userService.listTUser(userPageQuery);
    }

}
