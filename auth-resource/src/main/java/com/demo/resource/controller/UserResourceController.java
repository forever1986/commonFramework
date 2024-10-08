package com.demo.resource.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResourceController {

    @GetMapping("/listuser")
//    @PreAuthorize("hasAuthority('test')")
    public String list() {
        String user = "linmoo";
        return user;
    }

    @GetMapping("/listtenant")
    public String listtenant() {
        String tenant = "linmoo_tenant";
        return tenant;
    }

}
