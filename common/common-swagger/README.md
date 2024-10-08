# common-swagger
一个自动配置swagger的公共包

## Swagger2集成
1.引入springfox-swagger2和springfox-swagger-ui依赖  
```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```
2.配置SwaggerConfiguration，其中配置@EnableSwagger2
3.@Profile({"dev","test"})是根据环境配置
4.controller层要暴露到swagger中，只需要增加@ApiOperation(value = "测试echo")即可