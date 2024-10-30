package com.demo.common.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oss")
@Data
public class OssProperties {

    public static final String PREFIX = "oss";
    private String endpoint = "http://bucketname.endpoint";
    private String accessKey = "";
    private String secretKey = "";
    private String bucketName = "test";

}
