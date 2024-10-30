package com.demo.business.entity;

import com.demo.common.mongo.entity.Base;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

/**
 * 示例，一个mongo的collection
 */
@Data
@Document(collection = "project")
public class Project extends Base {
    public static String PROJECT_ID = "projectId";

    @Field
    private Long projectId; //对于业务实体的实际id

    @Field
    private String projectName;

    @Field
    private BigDecimal num;

    @Field
    private Person createPerson;

    @Override
    public Long getID() {
        return projectId;
    }
}
