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
