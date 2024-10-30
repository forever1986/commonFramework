# common-mongo
是一个集成mongoDB公共包，基本配置和增加Decimal128的转换，另外封装底层公用的BaseDAO，后续不同collection都可以继承BaseDAO（自动实现CURD）；

## 1 common-mongo配置
1）在common子模块下面创建common-mongo子模块，引入以下依赖：
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```
2）定义MongoConfigProperties，实现从yaml文件读取spring.data.mongodb.config下面的配置  
3）定义MongoConfiguration，实现MongoClientSettingsBuilderCustomizer个性化配置和MongoCustomConversions做Decimal128的转换  
4）分别定义BigDecimalToDecimal128Converter和Decimal128ToBigDecimalConverter做Decimal128和Decimal的相互转换  
5）定义DAO层的基础类BaseDAO，该类实现了mongo集合的CURD，业务类继承BaseDAO，需要注入MongoTemplate，重新赋值ID和clazz

## 2 business-biz引用
1）引入common-mongo子模块
```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>common-mongo</artifactId>
    <version>${project.version}</version>
</dependency>
```
2）在yaml文件配置mongo：
```yaml
spring:
  data:
    mongodb:
      database: commonFramework
      host: 127.0.0.1
      port: 27017
      config:
        minConnectionsPerHost: 5
        maxConnectionsPerHost: 20
```
3）定义业务实体类Project，注意Document注解是为了对于mongo的collection
```java
@Data
@Document(collection = "project")
public class Project extends Base {
    public static String PROJECT_ID = "projectId";

    @Field
    private Long projectId;

    @Field
    private String projectName;

    @Field
    private BigDecimal num;

    @Field
    private Person createPerson;

    @Override
    public Long getID() {
        return projectId;
    }
}
```
4）定义业务ProjectDAO:
```java
@Repository
public class ProjectDao extends BaseDAO<Project> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ProjectDao() {
        ID = "projectId";  //对于业务实体的实际id
        clazz = Project.class;
    }

    @Override
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
}
```
5）定义service和controller层进行测试