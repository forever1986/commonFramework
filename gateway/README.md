# gateway模块
是一个通过spring-cloud gateway加上nacos实现动态路由功能

## 1.原理说明
1）gateway是一个可以通过静态和动态配置的路由网关  
2）通过nacos-config存储配置文件（路由配置）  
3）通过nacos-discovery监听配置文件变化，调用gateway更新接口更新路由配置

## 2.配置流程
1）引入依赖的包(注意：引入spring-cloud-starter-gateway之后，并不需要在引入spring-boot-starter-web)
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```
2）配置bootstrap.yml
```yaml
server:
  port: 9991
spring:
  application:
    name: cloud-gateway-service
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yaml
        group: DEFAULT_GROUP
        prefix: ${spring.application.name}
```
3）设置RouteDefinitionRepository（自动监听和刷新路由表）  
4）设置GatewayApplication设置@EnableDiscoveryClient注册到nacos
5）配置nacos的路由配置
```json
[
  {
    "id": "baidu",
    "order": 0,
    "predicates": [{
      "args": {
        "pattern": "/baidu/**"
      },
      "name": "Path"
    }],
    "uri": "http://www.baidu.com",
    "filters": [{
      "args": {
        "_genkey_0": "2"
      },
      "name": "StripPrefix"
    }]
  },
  {
    "id": "manager",
    "predicates": [{
      "args": {
        "pattern": "/manager/**"
      },
      "name": "Path"
    }],
    "uri": "http://localhost:9981",
    "filters": [{
      "args": {
        "_genkey_0": "1"
      },
      "name": "StripPrefix"
    }]
  }
]

```

## 3.配置鉴权
1）ResourceServerManager(实现ReactiveAuthorizationManager)，设置鉴权操作  
2）定义SecurityGlobalFilter(实现GlobalFilter)，可以做一些事后处理  
3）配置ResourceServerConfig，注册securityWebFilterChain  
> **注意**： 网关对请求的主要处理流程包括ReactiveAuthenticationManager->ReactiveAuthorizationManager->Gateway Filters。  
> 1）其中ReactiveAuthenticationManager用于封装JWT为OAuth2Authentication并判断Token的有效性；  
> 2）ReactiveAuthorizationManager用于基于URL的鉴权；  
> 3）Gateway Filters是网关的过滤流程。

> **注意**：使用Google浏览器访问，会调用2次，是因为Google会做渲染预处理（第一次访问不会，后面则都会）；使用postman/IE浏览器则不会

## 4.增加JWT的验签
1）在ResourceServerConfig配置中RSA的公钥public.key（来自授权管理器auth-authorization生成的）
```java
/**
 * ServerHttpSecurity没有将jwt中authorities的负载部分当做Authentication
 * 需要把jwt的Claim中的authorities加入
 * 方案：重新定义权限管理器，默认转换器JwtGrantedAuthoritiesConverter
 */
@Bean
public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
}

/**
 * 本地获取JWT验签公钥
 */
@SneakyThrows
@Bean
public RSAPublicKey rsaPublicKey() {
    Resource resource = new ClassPathResource("public.key");
    InputStream is = resource.getInputStream();
    String publicKeyData = IoUtil.read(is).toString();
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec((Base64.decode(publicKeyData)));
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    RSAPublicKey rsaPublicKey = (RSAPublicKey)keyFactory.generatePublic(keySpec);
    return rsaPublicKey;
}
```
2）在方法securityWebFilterChain(ServerHttpSecurity http)设置解密
```java
http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
                // 本地获取公钥
                .publicKey(rsaPublicKey())
        ;
```
