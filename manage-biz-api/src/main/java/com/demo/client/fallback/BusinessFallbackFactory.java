package com.demo.client.fallback;

import com.demo.client.BusinessFeignClient;
import com.demo.common.core.result.Result;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BusinessFallbackFactory implements FallbackFactory<BusinessFeignClient> {


    @Override
    public BusinessFeignClient create(Throwable throwable) {
        return new BusinessFeignClient() {

            @Override
            public Result<String> business() {
                return Result.failed("Business服务的Business接口调用失败");
            }
        };
    }
}
