这是一个common工具模块，里面包括：  
1. common-log：是一个日志拦截器公共包，基于AOP+注解方式，其它项目可以引入并使用，自动在controller方法调用之前打印参数日志（当然也可以配置其它方法）；
2. common-mybatis：是一个集成mysql+mybatis-plus的公共包，里面配置了mybatis-plus的多租户插件、分页插件、防止全表更新与删除插件。 其中通过配置ignoreTables或者在mapper中配置注解@InterceptorIgnore(tenantLine = "true")则可以忽略多住户