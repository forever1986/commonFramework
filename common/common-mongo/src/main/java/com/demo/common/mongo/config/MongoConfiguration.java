package com.demo.common.mongo.config;

import com.demo.common.mongo.converter.BigDecimalToDecimal128Converter;
import com.demo.common.mongo.converter.Decimal128ToBigDecimalConverter;
import com.mongodb.client.MongoClient;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass({MongoClient.class})
@EnableConfigurationProperties({MongoConfigProperties.class})
@ConditionalOnMissingBean({MongoDatabaseFactory.class})
@NoArgsConstructor
public class MongoConfiguration {
    @Value("${spring.application.name}")
    private String applicationName;
    @Autowired
    private MongoConfigProperties properties;


    /**
     * 对mongo的详细配置：SSL、Pool、timeout、heartbeat
     * @return MongoClientSettingsBuilderCustomizer
     */
    @Bean
    public MongoClientSettingsBuilderCustomizer mongoDBDefaultSettings() {
        return (builder) -> {
            builder.applicationName(this.applicationName).applyToSslSettings((ssl) -> {
                ssl.enabled(this.properties.getSslEnabled()).invalidHostNameAllowed(this.properties.getSslInvalidHostNameAllowed());
            }).applyToConnectionPoolSettings((connectionPool) -> {
                connectionPool.maxSize(this.properties.getMaxConnectionsPerHost()).minSize(this.properties.getMinConnectionsPerHost()).maxWaitTime((long)this.properties.getMaxWaitTime(), TimeUnit.SECONDS).maxConnectionIdleTime((long)this.properties.getMaxConnectionIdleTime(), TimeUnit.SECONDS).maxConnectionLifeTime((long)this.properties.getMaxConnectionLifeTime(), TimeUnit.SECONDS).maintenanceInitialDelay((long)this.properties.getMaintenanceInitialDelay(), TimeUnit.SECONDS).maintenanceFrequency((long)this.properties.getMaintenanceFrequency(), TimeUnit.SECONDS);
            }).applyToSocketSettings((socket) -> {
                socket.connectTimeout(this.properties.getConnectTimeout(), TimeUnit.SECONDS).readTimeout(this.properties.getReadTimeout(), TimeUnit.SECONDS).receiveBufferSize(this.properties.getReceiveBufferSize()).sendBufferSize(this.properties.getSendBufferSize());
            }).applyToServerSettings((server) -> {
                server.heartbeatFrequency((long)this.properties.getHeartbeatFrequency(), TimeUnit.SECONDS).minHeartbeatFrequency((long)this.properties.getMinHeartbeatFrequency(), TimeUnit.SECONDS);
            });
        };
    }

    /**
     * mongo3.4版本之后的字段类型Decimal128的转换
     */
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        List<Object> converters = new ArrayList();
        converters.add(new BigDecimalToDecimal128Converter());
        converters.add(new Decimal128ToBigDecimalConverter());
        return new MongoCustomConversions(converters);
    }
}
