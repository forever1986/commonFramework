package com.demo.common.mongo.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "spring.data.mongodb.config"
)
@Data
@NoArgsConstructor
public class MongoConfigProperties {

    private Integer minConnectionsPerHost = 0;
    private Integer maxConnectionsPerHost = 100;
    private Integer maxWaitTime = 5;
    private Integer maxConnectionIdleTime = 300;
    private Integer maxConnectionLifeTime = 0;
    private Integer maintenanceInitialDelay = 5;
    private Integer maintenanceFrequency = 5;
    private Integer connectTimeout = 5;
    private Integer readTimeout = 5;
    private Integer receiveBufferSize = 52428800;
    private Integer sendBufferSize = 52428800;
    private Boolean sslEnabled = false;
    private Boolean sslInvalidHostNameAllowed = false;
    private Integer heartbeatFrequency = 10;
    private Integer minHeartbeatFrequency = 1;

}
