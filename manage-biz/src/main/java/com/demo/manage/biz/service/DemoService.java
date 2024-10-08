package com.demo.manage.biz.service;

import com.demo.manage.biz.utils.MessagesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Slf4j
public class DemoService {

    @Autowired
    private MessagesUtil messagesUtil;

    public String echo(@RequestParam String echo) {
        String projectName = messagesUtil.getMessage("project.name");
        log.info(projectName);
        log.info("echo======================"+echo);
        return echo;
    }
}
