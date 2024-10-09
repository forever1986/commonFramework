package com.demo.client;

import com.demo.client.constant.FeignClientConstant;
import com.demo.client.fallback.BusinessFallbackFactory;
import com.demo.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = FeignClientConstant.BUSINESS_URL, url = FeignClientConstant.BUSINESS_URL, fallbackFactory = BusinessFallbackFactory.class)
public interface BusinessFeignClient {

    @GetMapping("/business")
    Result<String> business();

}
