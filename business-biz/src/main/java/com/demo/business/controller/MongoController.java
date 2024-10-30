package com.demo.business.controller;

import com.demo.business.entity.Person;
import com.demo.business.entity.Project;
import com.demo.business.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试增删改查，对于mongo文档更新，其实可以统一个接口
 */
@RestController
public class MongoController {

    @Autowired
    ProjectService personService;

    @GetMapping("/getProject")
    public Project getPerson(){
        return personService.getProject(1L);
    }

    @GetMapping("/insertProject")
    public Project insertProject(){
        Project project = new Project();
        project.setProjectId(1L);
        project.setProjectName("firstProject");
        project.setNum(new BigDecimal(100));
        Person createPerson = new Person();
        createPerson.setPersonNo("N0001");
        createPerson.setPersonName("test");
        project.setCreatePerson(createPerson);
        return personService.insertProject(project);
    }

    @GetMapping("/updateProject")
    public Project updateProject(){
        Map<String,Object> updateMap = new HashMap<>();
        updateMap.put(Project.PROJECT_ID, 1L);
        updateMap.put("projectName", "更新项目");
        return personService.updateProject(updateMap);
    }

    @GetMapping("/deleteProject")
    public Project deleteProject(){
        return personService.deleteProject(1L);
    }

    @GetMapping("/queryProject")
    public List<Map> findProject(){
        List<String> fieldList = new ArrayList<>();
        fieldList.add("remark");
        fieldList.add("projectName");
        fieldList.add("createPerson");
        Criteria criteria = new Criteria();
        return personService.findProject(fieldList,criteria);
    }

}
