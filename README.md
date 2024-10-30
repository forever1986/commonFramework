# 1.脚手架
**init**：本项目中所需的外部资源，如nacos、mysql、redis等  
**auth-authentication**：是一个最简单oauth2+spring security的授权服务器  
**auth-github**：是一个基于oauth2使用github进行第三方授权认证登录  
**auth-security**：是一个基于spring-security框架集成的登录认证  
**auth-resource**：是一个基于oauth2的资源服务器  
**business-biz**：只是为了配合openfeign示例(manage-biz-api)，设置的第三方服务接口  
**common**：是一个集合多种工具的模块，工具包括：   
&ensp;&ensp;1）**common-amqp**：是一个公共包，主要封装使用RabbitMQ的配置。  
&ensp;&ensp;2）**common-core**：是一个公共包，主要定义常量使用，包括普通常量、异常常量。  
&ensp;&ensp;3）**common-exception**：是一个异常拦截器公共包，基于@RestControllerAdvice注解，定义各种异常处理器，避免给接口抛出异常；  
&ensp;&ensp;4）**common-log**：是一个日志拦截器公共包，基于AOP+注解方式，其它项目可以引入并使用，自动在controller方法调用之前打印参数日志（当然也可以配置其它方法）；  
其中@SysLog是一个注解，主要是为了方便设置方法在日志打印的属性（可以根据项目调整）  
&ensp;&ensp;5）**common-mybatis**：是一个集成mysql+mybatis-plus的公共包，里面配置了mybatis-plus的多租户插件、分页插件、防止全表更新与删除插件。
其中通过配置ignoreTables或者在mapper中配置注解@InterceptorIgnore(tenantLine = "true")则可以忽略多住户    
&ensp;&ensp;6）**common-oss**：是一个集成minIO对象存储的公共包，自动注册操作oss的API，其它子模块直接引用并配置yaml即可访问API
&ensp;&ensp;7）**common-redis**：是一个集成redis配置的公共包，里面配置redistemplate相关默认信息  
&ensp;&ensp;8）**common-swagger**：是一个集成swagger配置的公共包，里面配置swagger以及环境生效等内容  
**gateway**：微服务的网关，配置nacos实现动态配置网关功能  
**IoT**：是一个访问EMQX的MQTT broker范例  
**manage-biz**：是一个模拟业务的范例，里面实现了多环境配置（nacos）、国际化、日志logback、swagger、junit单元测试、数据库；引用common-log、common-mybatis、common-swagger等。  
**manage-biz-api**：是一个抽离出来的访问第三方接口，使用openfeign进行访问（这是实践中常用的范例）  
**seata-demo**：是一个分布式事务的demo工程

> **_注意：本案例中包括项目之外的资源，包括nacos、mysql、redis，其配置都详细记录在init子模块中_**


# 2.多模块配置
现在spring-boot支持多模块，在比较大的项目中，多模块可以统一引用、解耦、解决代码重复问题。  
配置多模块时，有几个点需要注意
## 一、spring-boot和spring-cloud
spring-boot-starter和spring-boot-dependencies，spring-boot-starter是继承spring-boot-dependencies,也就是说2者功能一样。
一般spring-boot-starter是在parent标签中使用，项目只能继承1个parent，所以如果项目已经有parent，
那么只能使用spring-boot-dependencies(注意：spring-boot-dependencies只能在dependencyManagement中引用)

spring cloud情况也和spring-boot一样

## 二、Maven pom文件
1. dependencyManagement标签：用于定义引入依赖的版本号，一般用于父项目，在父项目中统一项目中所有的依赖版本
2. Maven根据不同阶段做不同时期，不同阶段有对应的plugin。其中重要的有clean、package、install
3. 不同plugin有不同功能，spring-boot-maven-plugin是spring-boot为了能够打出直接运行的jar实现的
4. install可以将项目打成jar包，放入本地仓库，其它依赖于它的代码才可以做package

# 3.其它功能

## 3.1 多环境情况（nacos方案）
详情见manage-biz模块

## 3.2 Slf4j+logback+AOP集成统一日志
详情见manage-biz项目+common-log模块

## 3.3 Swagger2集成
详情见manage-swagger模块

## 3.4 spring-boot的mysql使用druid连接池
详情见manage-biz模块+common-mybatis模块

## 3.5 JUnit测试用例集成
详情见manage-biz模块

## 3.6 HandlerInterceptor拦截器统一入口认证
详情见manage-biz模块

## 3.7 国际化
详情见manage-biz模块

## 3.8 spring security集成（用户和权限控制）
详情见auth-security模块

## 3.9 oauth2（统一登录）
### oauth2授权服务器
详情见auth-authentication模块

### oauth2资源服务器
详情见auth-resource模块

### oauth2客户端
详情见auth-gihub模块

## 3.10 gateway集成nacos实现动态路由
详情见gateway模块

## 3.11 redis集成
详情见common-redis和manage-biz模块