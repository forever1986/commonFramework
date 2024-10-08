# common-mybatis
是一个集成mysql+mybatis-plus的公共包
## MyBatis-plus配置
1.在MyBatisConfig配置多个插件  
2.多租户插件（TenantLineInnerInterceptor），通过读取配置，看是否开启多租户以及多租户的字段名等配置  
3.分页插件（PaginationInnerInterceptor），加入插件之后，可以使用selectPage等方法进行自动分页  
4.防止全表更新与删除插件（BlockAttackInnerInterceptor），防止全表更新删除操作
