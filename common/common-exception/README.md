# common-exception
是一个异常拦截器公共包，基于@RestControllerAdvice注解，定义各种异常处理器，避免给接口抛出异常；

## 操作方法
1.pom文件引入
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.example</groupId>
    <artifactId>common-core</artifactId>
    <version>${project.version}</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```
2.自定义一个异常BizException（当然看自己业务需求定义不同异常）  
3.设置GlobalExceptionHandler，加入@RestControllerAdvice注解，并使用@ExceptionHandler处理各种异常  
4.在spring.factories中注入GlobalExceptionHandler  
5.业务模块，如manage-biz在pom引入common-exception子模块，则会生效  
6.在manage-biz的DemoController中增加testException方法测试异常处理  
