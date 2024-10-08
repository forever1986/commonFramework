package com.demo.client;

import com.demo.client.config.FeignConfig;
import com.demo.client.constant.FeignClientConstant;
import com.demo.client.fallback.BusinessFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = FeignClientConstant.BUSINESS_BIZ_SERVICE, fallbackFactory = BusinessFallbackFactory.class, configuration = FeignConfig.class)
public interface BusinessByServiceNameFeignClient {

    @GetMapping("/business")
    String business();

}
