package com.demo.business.dao;

import com.demo.business.entity.Project;
import com.demo.common.mongo.dao.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDao extends BaseDAO<Project> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ProjectDao() {
        ID = "projectId";
        clazz = Project.class;
    }

    @Override
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
}
