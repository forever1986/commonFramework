package com.demo.sharding.controller;

import com.demo.common.core.result.Result;
import com.demo.sharding.entity.TestOrder;
import com.demo.sharding.mapper.TestOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@Slf4j
public class TestOrderController {

    @Resource
    private TestOrderMapper testOrderMapper;

    @GetMapping("/test")
    public Result<String> test() {
        log.info("test======================");
        for (int i = 1; i < 10; i++) {
            TestOrder order = new TestOrder();
//            order.setOrderId(i+231);
            order.setOrderName("订单"+i);
            order.setGood("商品"+i);
            order.setPrice(new BigDecimal(1998+i));
            testOrderMapper.insert(order);
        }
        return Result.success();
    }
}
