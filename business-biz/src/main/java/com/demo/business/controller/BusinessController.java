package com.demo.business.controller;

import com.demo.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BusinessController {


    @GetMapping("/business")
    public Result<String> business() {
        log.info("call remote business");
        return Result.success("remote business");
    }

}
