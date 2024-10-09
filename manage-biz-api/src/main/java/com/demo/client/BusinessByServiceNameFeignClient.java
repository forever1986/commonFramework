package com.demo.client;

import com.demo.client.config.FeignConfig;
import com.demo.client.constant.FeignClientConstant;
import com.demo.client.fallback.BusinessByServiceNameFallbackFactory;
import com.demo.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = FeignClientConstant.BUSINESS_BIZ_SERVICE, fallbackFactory = BusinessByServiceNameFallbackFactory.class, configuration = FeignConfig.class)
public interface BusinessByServiceNameFeignClient {

    @GetMapping("/business")
    Result<String> business();



}
