package com.demo.manage.biz.controller;

import com.demo.client.BusinessByServiceNameFeignClient;
import com.demo.client.BusinessFeignClient;
import com.demo.common.core.result.Result;
import com.demo.common.core.result.ResultCode;
import com.demo.common.exception.BizException;
import com.demo.common.log.enums.ModuleTypeEnum;
import com.demo.common.log.aspect.SysLog;
import com.demo.manage.biz.constant.NacosValueConstant;
import com.demo.manage.biz.constant.UserHolder;
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

    @Autowired
    private BusinessFeignClient businessFeignClient;

    @Autowired
    private BusinessByServiceNameFeignClient businessByServiceNameFeignClient;

    @SysLog(module= ModuleTypeEnum.MANAGE, description="测试echo")
    @ApiOperation(value = "测试echo")
    @GetMapping("/echo")
    public Result<String> echo(@RequestParam String echo) {
        log.info("echo======================"+echo);
        log.info(nacosValueConstant.getValue());
        log.info(nacosValueConstant.getTest());
        log.info(UserHolder.getTUser().getUsername());
        return Result.success(demoService.echo(echo));
    }


    @SysLog(module= ModuleTypeEnum.MANAGE, description="测试openfeign")
    @ApiOperation(value = "测试openfeign")
    @GetMapping("/remote/business")
    public Result<String> remoteBusiness() {
        return businessFeignClient.business();
    }

    @SysLog(module= ModuleTypeEnum.MANAGE, description="测试openfeign访问服务名")
    @ApiOperation(value = "测试openfeign访问服务名")
    @GetMapping("/remote/businessbyservicename")
    public Result<String> remoteBusinessByServiceName() {
        return businessByServiceNameFeignClient.business();
    }

    @SysLog(module= ModuleTypeEnum.MANAGE, description="测试全局异常处理")
    @ApiOperation(value = "测试全局异常处理")
    @GetMapping("/testexception")
    public Result<String> testException(Boolean isNotNull) {
        log.info("call testException");
        if(isNotNull==null || !isNotNull){
            throw new BizException(ResultCode.PARAM_IS_NULL);
        }
        return Result.success("ok");
    }
}
