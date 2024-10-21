# manage-biz
manage-biz：是一个模拟业务的范例，里面实现了多种功能，包括：  
1）多环境配置（nacos）  
2）国际化  
3）junit单元测试  
4）拦截器功能（模拟验证权限）  
5）日志logback（与common-log模块配合做自动化切面日志）  
6）swagger（与common-swagger模块配合实现swagger功能）  
7）集成mybatis-plus和mysql数据库（与common-mybatis模块配合实现mybatis的druid连接池、多租户、分页等功能）  

## 一、多环境情况（nacos方案）
（不采用nacos方案）

1.在resources中设置不同的application-dev.yaml、application-test.yaml、application-prod.yaml  
2.在主application.yaml中配置spring.profiles.active:XXX，就可以选择哪个环境或者在运行的idea中设置-Dspring.profiles.active=XXX

（采用nacos方案）  
1.实际开发中，为了不将太多配置信息暴露在代码中，往往使用配置中心来设置，这里使用nacos  
2.nacos设置多环境有不同的方案：分别可以使用groupID、namespace或者不同nacos来作为不同环境配置  
3.这里采用使用namespace来设置不同项目，groupID来设置不同环境  
1）使用bootstrap.yml配置文件（不能使用application.ym文件）
2）配置nacos的数据
```yaml
spring:
  # nacos配置
  cloud:
    nacos:
      config:
        server-addr: 127.0.0.1:8848
        namespace: manage-biz
        group: ${spring.profiles.active}
        file-extension: yaml
        username: manage-biz
        password: manage-biz
#        prefix: ${spring.application.name}
        shared-configs[0]:
          data-id: common.yaml
          refresh: true
          namespace: manage-biz
          group: ${spring.profiles.active}
```
3）在nacos中创建manage-biz的namespace，同时分配一个用户（manage-biz）和权限
4）在nacos中的manage-biz命名空间下分别创建不同groupid(dev、test、prod)的cloud-manage-biz-service.yaml文件  
注意：默认会通过spring.application.name在nacos的文件名可以是的cloud-manage-biz-service或者cloud-manage-biz-service.yaml或者cloud-manage-biz-service-dev.yaml都能读取得到(顺序按照这3个的顺序)。
shared-configs配置，不需要配置file-extension，而是通过data-id的后缀名来决定

## 二、国际化
1.在resources下面建立i18n文件  
2.分别设置messages.properties、messages_en_US.properties、messages_zh_CN.properties  
3.在application.yml中配置spring:messages:basename: i18n/messages  
4.注册MessageSource messageSource。使用messageSource.getMessage(key, null, LocaleContextHolder.getLocale());

## 三、JUnit测试用例集成
1.引入spring-boot-starter-test(会自动加入junit、mockito等测试框架)  
2.在test下建立与src的目录结构  
3.建立Test类，可以自行选择junit、mockito等测试框架

## 四、HandlerInterceptor拦截器统一入口认证
1.LoginInterceptor继承HandlerInterceptor，重写preHandle方法做登录认证  
2.WebConfig实现WebMvcConfigurer，通过重写addInterceptors，将loginInterceptor注册到监听器

## 五、Slf4j+logback+AOP集成统一日
1.引入引用common-log模块，做日志切面  
2.在resources中增加logback-spring.xml，配置logback日志打印  

## 六、Swagger2集成
1.引用common-swagger模块，增加swagger功能
2.在controller层增加@ApiOperation(value = "测试echo")即可加入swagger中
3.不同环境开启或者关闭swagger，在common-swagger项目的SwaggerConfiguration中@Profile({"dev","test"})已经配置完成

## 七、spring-boot的mysql使用druid连接池
1.引入druid的spring-boot默认配置版本（本次案例在common-mybatis模块中引入，因此不需要再引入）
```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
</dependency>
```
2.yml配置文件中配置上druid的配置
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/commonframework?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root
    druid:
      initial-size: 5
      min-idle: 5
      maxActive: 20
      maxWait: 3000
      timeBetweenEvictionRunsMillis: 60000
      minEvictableIdleTimeMillis: 300000
      validationQuery: select 'x'
      testWhileIdle: true
      testOnBorrow: false
      testOnReturn: false
      poolPreparedStatements: false
      filters: stat,wall,slf4j
      connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000;socketTimeout=10000;connectTimeout=1200
```

## 八、Flyway集成
我们的代码有git做版本管理，但是我们的sql脚本呢？尤其是各类ddl脚本并没有进行版本的管。当我们的项目从一个环境迁移到另一个或者dev、test各个环境部署，都需要先将脚本执行一遍这是极其不方便，而且也会经常遗漏。那么Flyway就派上用场。  
Flyway一个开源的数据库迁移工具，用于管理和执行数据库结构的版本变更。通俗来说，它帮助开发者跟踪和应用数据库中的更改，比如表的创建、列的修改等。主要的功能为：
- 数据库版本控制
- 自动迁移
- 迁移历史管理

1）在manage-biz子模块中引入依赖（注意这里没有版本号，是因为在父项目commonFramework中已经引入）
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

2）yaml文件配置  
数据库配置原先已经存在nacos的多环境配置中，因此不用再此配置。只需要配置flyway。
> 注意：我们可以在nacos多环境的cloud-manage-biz-service分别dev、test、prod配置，因为不同环境要求可能不一样，**特别是生产环境，一定不要使用flyway方式执行**

```yaml
spring:
  # 配置flyway（数据库sql版本控制）
  flyway:
    # 开关，建议在prod环境关闭
    enabled: true
    #数据库存在表时，自动使用设置的基线版本（baseline-version），数据库不存在表时，即使设置了，也会从第一个版本开始执行，默认值为false
    baseline-on-migrate: true
    #基线版本号，baseline-on-migrate为true时小于等于此版本号的脚本不会执行，默认值为1
    baseline-version: 1.0.0
    #设置为false会删除指定schema下所有的表，生产环境务必禁用，spring中该参数默认是false，需要手动设置为true
    clean-disabled: true
    #sql脚本存放位置，允许设置多个location，用英文逗号分割，默认值为classpath:db/migration
    locations: classpath:db/migration
    #是否替换sql脚本中的占位符，占位符默认是${xxx},默认是替换，如果不需要替换，可以设置为false
    placeholder-replacement: false
```
3）在resources下面建立db/migration，写入sql文件
> 注意：flyway的版本号命名规则，以V/U/R开头，后面接数字、点、下划线都可以，再接双下划线，再接描述。  
> 比如：V1.0.1__init_test_table.sql，其中1.0.1是对应baseline-version: 1.0.0,init_test_table是描述

## 九、redis集成
1）引入子模块common-redis
```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>common-redis</artifactId>
    <version>${project.version}</version>
</dependency>
```
2）在application.yml文件中配置redis服务器(manage-biz配置都在nacos，因此可以在nacos配置)
```yaml
spring:
  # redis配置
  redis:
    database: 1
    host: 127.0.0.1
    port: 6379
    password:
    timeout: 30000
    client-type: jedis
    jedis:
      pool:
        max-active: 1000
        max-idle: 100
        min-idle: 0
        maxWait: 1000
```
3）只需要直接使用RedisTemplate或者StringRedisTemplate即可，本实例使用TestRedisService和TestRedisController做演示  
