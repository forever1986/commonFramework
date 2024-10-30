# common-oss
是一个集成minIO对象存储的公共包

## 1 配置自动化的OSS对象存储公共包
1）在common子模块下新建common-oss子模块，以spring.factories发布  
2）引入以下依赖：
```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
<dependency>
    <groupId>org.example</groupId>
    <artifactId>common-core</artifactId>
    <version>${project.version}</version>
</dependency>
<dependency>
    <groupId>org.example</groupId>
    <artifactId>common-exception</artifactId>
    <version>${project.version}</version>
</dependency>
```
3）配置OssProperties读取yaml文件的对象存储相关配置  
4）配置MinioConfiguration，注册OssTemplate（访问oss）和OssEndpoint（对外接口）  
5）新建OssTemplate，作为封装oss底层操作
6）新建OssEndpoint，作为对外的API接口

## 2 business-biz引用
1）引入common-oss子模块
```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>common-oss</artifactId>
    <version>${project.version}</version>
</dependency>
```
2）在yaml文件配置以下信息
```yaml
oss:
  enabled: true  # 是否开启oss存储配置
  api: true  # 自动开启API接口
  endpoint: http://127.0.0.1:9005  # MinIO服务器的URL
  access-key: minioadmin      # 访问密钥
  secret-key: minioadmin      # 密钥密码
  bucket-name: works     # 默认的Bucket名称
```
3）直接就可以使用OssEndpoint的接口操作oss

## 3 注意坑点
1）**坑1**：okhttp版本冲突，如果项目引入openfeign或者springcloud中使用okhttp与io.minio里面的okhttp可能会发生冲突，需要在父项目commonFramework声明okhttp统一版本  
```xml
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>${okhttp.version}</version>
</dependency>
```
2）**坑2**：调创建等操作oss时，报错：DateTimeParseException: TemporalAccessor。
> 这是jdk1.8早期版本的一个bug，需要升级到jdk1.8的小版本到22之后，比如我原先是jdk-8u5，升级为jdk-8u431即可