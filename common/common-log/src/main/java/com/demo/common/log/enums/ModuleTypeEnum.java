package com.demo.common.log.enums;

import lombok.Getter;


public enum ModuleTypeEnum {

    MANAGE("MANAGE", "管理");


    @Getter
    private final String code;

    @Getter
    private final String name;

    ModuleTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
