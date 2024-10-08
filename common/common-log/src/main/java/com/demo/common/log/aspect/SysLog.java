package com.demo.common.log.aspect;


import java.lang.annotation.*;
import com.demo.common.log.enums.ModuleTypeEnum;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 操作模块
     */
    ModuleTypeEnum module();


    /**
     * 操作描述
     */
    String description() default "";
}
