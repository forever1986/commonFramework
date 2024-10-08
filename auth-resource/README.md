# auth-resource
是一个基于oauth2的资源服务器  

## 1.资源服务器的原理
通过访问远程授权服务器的 http://localhost:8080/oauth/check_token 接口获得权限。  
有2种配备方式：  
1）一种是采用yml+注解方式  
2）一种是采用ResourceServerConfigurerAdapter方式

## 2.操作步骤（采用yml+注解方式）
1.引入相关的包
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!-- oauth2 服务端 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>
```
2.yml文件增加访问远程授权服务器的注解
```yaml
security:
  oauth2:
    client:
      #基操
      client-id: client-lq
      client-secret: secret-lq
    resource:
      #因为资源服务器得验证你的Token是否有访问此资源的权限以及用户信息，所以只需要一个验证地址
      token-info-uri: http://localhost:8080/oauth/check_token
      id: auth-resource1
```
3.在ResourceApplication中配置@EnableResourceServer注解  
4.新建一个demo接口UserResourceController，如果在授权服务器配置了authorities，则可以在方法前面增加@PreAuthorize("hasAuthority('read:user')")来控制方法级别的权限

## 3.权限配置说明
第1层资源级别：id: auth-resource1;与授权服务器中字段resource_ids配置相对应  
第2层读写多端级别：scope=all，表示访问该资源服务器中的某些资源所需的权限；与授权服务器中的字段scope配置相对应  
第3层方法级别：.antMatchers("/product/*").hasAuthority("product")或者@PreAuthorize("hasAuthority('test')")；表示具体某个接口所需的权限；与授权服务器的字段authorities相对应
