package com.demo.business.service;

import com.demo.business.dao.ProjectDao;
import com.demo.business.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    @Autowired
    ProjectDao projectDao;

    public Project getProject(Long id){
        return projectDao.findById(id);
    }

    public Project insertProject(Project project){
        return projectDao.insertDocument(project);
    }

    public Project updateProject(Map<String,Object> updateMap){
        return projectDao.updateDocument(updateMap);
    }

    public Project deleteProject(Long id){
        return projectDao.deleteDocument(id);
    }

    public List<Map> findProject(List<String> fieldList, Criteria criteria){
        List<Map> list = projectDao.findDocument(fieldList,criteria);
        return list;
    }

}
