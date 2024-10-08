package com.demo.business.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {


    @GetMapping("/business")
    public String business() {
        return "remote business";
    }

}
