package com.demo.business.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 演示多重嵌套
 */
@Data
public class Person {

    @Field
    private String personNo;

    @Field
    private String personName;

}
