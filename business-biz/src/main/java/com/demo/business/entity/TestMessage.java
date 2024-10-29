package com.demo.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 测试使用消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestMessage {
    private int id;
    private String message;
    private Timestamp createTime;


}
