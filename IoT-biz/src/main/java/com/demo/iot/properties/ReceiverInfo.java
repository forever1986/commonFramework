package com.demo.iot.properties;

import lombok.Data;

@Data
public class ReceiverInfo {

    private String client_id;
    private String topic;
    private Integer qos;
}
