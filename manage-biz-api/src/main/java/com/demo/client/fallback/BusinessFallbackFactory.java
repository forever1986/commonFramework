package com.demo.client.fallback;

import com.demo.client.BusinessFeignClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BusinessFallbackFactory implements FallbackFactory<BusinessFeignClient> {


    @Override
    public BusinessFeignClient create(Throwable throwable) {
        return new BusinessFeignClient() {

            @Override
            public String business() {
                return "Business服务的Business接口调用失败";
            }
        };
    }
}
