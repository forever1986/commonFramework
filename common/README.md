这是一个common工具模块，里面包括：  
1. common-amqp：是一个公共包，主要是自动配置RabbitMQ消息队列。
2. common-core：是一个公共包，主要定义常量使用，包括普通常量、异常常量。
3. common-exception：是一个异常拦截器公共包，基于@RestControllerAdvice注解，定义各种异常处理器，避免给接口抛出异常；  
4. common-log：是一个日志拦截器公共包，基于AOP+注解方式，其它项目可以引入并使用，自动在controller方法调用之前打印参数日志（当然也可以配置其它方法）；
5. common-mybatis：是一个集成mysql+mybatis-plus的公共包，里面配置了mybatis-plus的多租户插件、分页插件、防止全表更新与删除插件。 其中通过配置ignoreTables或者在mapper中配置注解@InterceptorIgnore(tenantLine = "true")则可以忽略多住户  
6. common-oss：是一个集成minIO对象存储的公共包，自动注册操作oss的API
7. common-redis：是一个集成redis的公共包，默认定义redis有2种不同的template
8. common-swagger：是一个集成swagger配置的公共包，里面配置swagger以及环境生效等内容