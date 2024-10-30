package com.demo.common.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Configuration
public class MyBatisConfiguration {

    @Value("${tenant.enable:false}")
    private Boolean tenantEnable; //是否开启多租户
    @Value("${tenant.headAttr:tenant-code}")
    private String tenantHeader;  //多租户的租户id获取
    @Value("${tenant.column:tenant_code}")
    private String tenantColumn;  //多租户对应表的列名
    @Value("${tenant.ignoreTables:}")
    private String tenantIgnoreTables; //忽视多租户表

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 多租户插件
        if(tenantEnable != null && tenantEnable){
            mybatisPlusInterceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
                @Override
                public Expression getTenantId() {
                    ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    assert attributes != null;
                    HttpServletRequest request = attributes.getRequest();
                    String tenantId = request.getHeader(tenantHeader);
                    if(StringUtils.isBlank(tenantId)){
                        log.error("request中无租户信息={},",request.getRequestURL().toString());
                        throw new RuntimeException(String.format("request中%s无值",tenantHeader));
                    }
                    return new StringValue(tenantId);
                }

                // 设置数据库中租户列名
                @Override
                public String getTenantIdColumn() {
                    return tenantColumn;
                }
                // 过滤不需要拼多租户条件的表
                @Override
                public boolean ignoreTable(String tableName) {
                    return Arrays.stream(tenantIgnoreTables.split(",")).anyMatch(e->e.equalsIgnoreCase(tableName));
                }
            }));
        }
        // 分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 防止全表更新与删除插件
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return mybatisPlusInterceptor;
    }

}
