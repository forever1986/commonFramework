# manage-biz-api
是一个抽离出来的访问第三方接口，使用openfeign进行访问（这是实践中常用的范例）  
通过单独子模块将openfeign调用远程应用接口独立出来，也是一种实践的经验

# 1.理论知识
以前你调用第三方应用接口的时候，或许可以使用httpclient方式，或者resttemplate方式，但是这些操作都比较繁琐，在微服务时代，诞生了feign。  
**feign**是一个声明式Web服务客户端，它使编写Web服务客户端更加容易。可以让你定义一个接口并通过注解方式，就能调用远程接口，同时还集成负载均衡、连接池、熔断等功能。Feign 最早是由 Netflix 公司进行维护的，后来 Netflix 不再对其进行维护，最终 Feign 由社区进行维护，更名为 **Openfeign**。

# 2.操作实践
本次实操是通过manage-biz调用business-biz服务的business接口（新建business-biz只是定义一个普通的接口）
![img.png](readme-img/openfeign.png)  
## 2.1 新建business-biz服务
1）新建子模块business-biz，配置pom
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```
2）配置yaml文件
```yaml
server:
  port: 9982
spring:
  application:
    name: business-biz-service

```
3）新建一个controller接口BusinessController
## 2.2 新建manage-biz-api
1）新建manage-biz-api子模块，在pom配置中引入：(**注意**：如果引入loadbalancer负载均衡，则需要排除ribbon.你也可以在yml文件中配置不启用ribbon)
```xml
<!--openFeign-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
    <exclusions>
        <!-- 排除ribbon负载均衡，使用loadbalancer -->
        <exclusion>
            <artifactId>spring-cloud-netflix-ribbon</artifactId>
            <groupId>org.springframework.cloud</groupId>
        </exclusion>
    </exclusions>
</dependency>
<!--负载均衡器-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```
2）在manage-biz-api子模块上，定义一个与business接口一致的feign客户端BusinessFeignClient  
3）在manage-biz-api子模块上，定义一个错误回调方法BusinessFallbackFactory，用于如果business-biz服务的business接口无法提供服务时的处理机制  
4）在spring.factories上面注册BusinessFallbackFactory
## 2.3 manage-biz子模块调用openfeign
1）在manage-biz子模块上，pom文件引入manage-biz-api子模块  
2）在manage-biz子模块上，启动类ManageApplication引入注解@EnableFeignClients(basePackages = {"com.demo.client"})，里面声明feign包地址

# 3.使用服务注册方式调用
如果采用服务注册方式调用（也就是不通过ip地址），那么就需要使用微服务那一套，其实就是将服务注册到注册中心）
## 3.1 将business-biz注册到nacos上
1）在business-biz的pom文件加入nacos依赖
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```
2）在business-biz的yml文件中配置nacos信息
```yaml
server:
  port: 9982
spring:
  application:
    name: business-biz-service
  # nacos配置
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848 # nacos地址
        namespace: manage-biz
        username: manage-biz
        password: manage-biz

```
3）启动business-biz服务，在nacos的服务列表中，可看见business-biz-service服务
## 3.2 manage-biz通过服务名称调用
1）需要将manage-biz也注册到微服务上面
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```
2）注意namespace命名空间和group分组必须与要访问的business-biz一致  
3）在yaml文件中配置discovery的配置
```yaml
spring:
  # nacos服务注册与发现
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        username: nacos
        password: nacos
```
## 3.3 manage-biz-api增加通过服务名称访问的接口
1）新增BusinessByServiceNameFeignClient和回调类BusinessByServiceNameFallbackFactory，注意里面的配置是通过服务名  
2）在spring.factories中注入BusinessByServiceNameFallbackFactory类  
3）在manage-biz子模块的DemoController上面增加一个通过服务名称访问的接口
```java
@SysLog(module= ModuleTypeEnum.MANAGE, description="测试openfeign访问服务名")
@ApiOperation(value = "测试openfeign访问服务名")
@GetMapping("/remote/businessbyservicename")
public String remoteBusinessByServiceName() {
    return businessByServiceNameFeignClient.business();
}
```

# 4.高阶使用
## 4.1 负载均衡
使用openfeign比较低的版本时，默认是使用ribbon作为负载均衡，但是ribbon已经不再维护，因此大多数情况下使用loadbalancer作为负载均衡。使用loadbalancer作为负载均衡有几个点需要注意
1）如果你的spring cloud版本较高，则默认引入loadbalancer
2）如果你的spring cloud版本较低（本案例就是默认引入ribbon的），那么你需要手动引入loadbalancer，同时还需要关闭或者排除ribbon
```xml
<!--openFeign-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
    <exclusions>
        <!-- 排除ribbon负载均衡，使用loadbalancer -->
        <exclusion>
            <artifactId>spring-cloud-netflix-ribbon</artifactId>
            <groupId>org.springframework.cloud</groupId>
        </exclusion>
    </exclusions>
</dependency>
<!--负载均衡器-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```
## 4.2 连接池
openfeign默认的HTTP请求是HttpURLConnection，而HttpURLConnection不支持连接池。如果你的服务只是偶尔调用，那么是否使用连接池影响不大，但是如果你的服务在频繁，那么使用连接池的好处就会显现出来。
openfeign支持的连接池包括：Apache HttpClient和OKHttp。下面就以OKHttp为例，搭建openfeign的连接池。  
1）在manage-biz-api子模块的pom中引入okhttp
```xml
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-okhttp</artifactId>
</dependency>
```
2）在manage-biz的yaml文件中启用okhttp
```yaml
feign:
  # 开启OKHttp功能
  okhttp:
    enabled: true
```
## 4.3 日志配置
### 4.3.1 日志级别
openfeign的日志级别包括以下几种：
- NONE：不记录任何日志（默认值）。
- BASIC：仅记录请求方法和URL以及响应状态码和执行时间。
- HEADERS：记录基本信息加上请求和响应的头信息。
- FULL：记录请求和响应的头信息、正文和元数据。
### 4.3.2 配置client所在包日志级别
一般日志都是用于调试使用，如果你项目集成logback时，你先要将openfeign的client所在包的日志级别定义为debug
比如该项目中
```yaml
logging:
  level:
    com.demo.client: debug
```
### 4.3.3 通过java代码配置
1）定义一个FeignConfig的类，返回一个Bean，在里面设置日志级别
2）在对于的client，比如BusinessByServiceNameFeignClient，设置configuration = FeignConfig.class
### 4.3.4 通过yaml文件配置
除了java代码配置之外，也可以使用yaml文件配置
```yaml
feign:
  client:
    config:
      applicationname: #这个地方改为你的BusinessByServiceNameFeignClient里面value值
        logger-level: full

```

## 4.4 熔断处理
openfeign本身集成了hystrix或者sentinel。其中hystrix适合于java和单机情况；而sentinel适合多种语言，并且可以独立部署服务器。因此根据你项目大小和需求，可以设置不同限流  
下面以集成hystrix为例：  
1）在manage-biz的yaml文件中配置开启hystrix
```yaml
feign:
  hystrix:
    enabled: true
```
2）写一个BusinessFallbackFactory实现FallbackFactory<Client>，这样在重写接口方法里面
```java
package com.demo.client.fallback;

import com.demo.client.BusinessFeignClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BusinessFallbackFactory implements FallbackFactory<BusinessFeignClient> {


    @Override
    public BusinessFeignClient create(Throwable throwable) {
        return new BusinessFeignClient() {

            @Override
            public String business() {
                return "Business服务的Business接口调用失败";
            }
        };
    }
}

```

## 4.5 其它配置
超时设置
```yaml
feign:
    client:
        config:
          default:
            #连接超时时间，默认2s
            connect-timeout: 5000
            #请求处理超时时间，默认5s
            read-timeout: 10000
```