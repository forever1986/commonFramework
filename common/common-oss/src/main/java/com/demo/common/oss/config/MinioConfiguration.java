package com.demo.common.oss.config;

import com.demo.common.oss.controller.OssEndpoint;
import com.demo.common.oss.service.OssTemplate;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class MinioConfiguration {

    @Autowired
    private OssProperties properties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(properties.getEndpoint()).credentials(properties.getAccessKey(), properties.getSecretKey()).build();
    }

    @Bean
    @ConditionalOnMissingBean({OssTemplate.class})
    @ConditionalOnProperty(
            prefix = "oss",
            name = {"enabled"},
            havingValue = "true",
            matchIfMissing = true
    )
    public OssTemplate ossTemplate(MinioClient minioClient) {
        return new OssTemplate(this.properties, minioClient);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "oss",
            name = {"api"},
            havingValue = "true"
    )
    public OssEndpoint ossEndpoint(OssTemplate template) {
        return new OssEndpoint(template);
    }

}
