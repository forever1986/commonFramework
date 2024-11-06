package com.demo.sharding.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("test_order")
public class TestOrder {

    private Long orderId;

    private String orderName;

    private String good;

    private BigDecimal price;

}
