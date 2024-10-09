package com.demo.client.fallback;

import com.demo.client.BusinessByServiceNameFeignClient;
import com.demo.common.core.result.Result;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BusinessByServiceNameFallbackFactory implements FallbackFactory<BusinessByServiceNameFeignClient> {


    @Override
    public BusinessByServiceNameFeignClient create(Throwable throwable) {
        return new BusinessByServiceNameFeignClient() {

            @Override
            public Result<String> business() {
                return Result.failed("Business服务的Business接口调用失败");
            }
        };
    }
}
