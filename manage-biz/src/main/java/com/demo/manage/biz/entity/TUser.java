package com.demo.manage.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 表user结构
 */
@Data
public class TUser {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String tenantCode;

    private String username;

    private String password;

    private String email;

}
