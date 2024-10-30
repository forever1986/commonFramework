package com.demo.common.mongo.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public abstract class Base {

    @Field
    protected Boolean del_flag = false;

    public abstract Long getID();
}
