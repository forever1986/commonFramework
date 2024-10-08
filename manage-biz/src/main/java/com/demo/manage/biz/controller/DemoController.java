package com.demo.manage.biz.controller;

import com.demo.common.log.enums.ModuleTypeEnum;
import com.demo.common.log.aspect.SysLog;
import com.demo.manage.biz.constant.NacosValueConstant;
import com.demo.manage.biz.service.DemoService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DemoController {


    @Autowired
    private DemoService demoService;

    @Autowired
    private NacosValueConstant nacosValueConstant;

    @SysLog(module= ModuleTypeEnum.MANAGE, description="测试echo")
    @ApiOperation(value = "测试echo")
    @GetMapping("/echo")
    public String echo(@RequestParam String echo) {
        log.info("echo======================"+echo);
        log.info(nacosValueConstant.getValue());
        log.info(nacosValueConstant.getTest());
        return demoService.echo(echo);
    }
}
