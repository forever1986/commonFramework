# auth-authentication
鉴权服务器：一个基于oauth2的鉴权服务器（授权码），集成spring-security+jwt做认证  

# oauth2基本知识
oauth2其实是一种协议，通过这种协议实现认证和鉴权过程  
（**注意**：这里很容易和security弄混，其实security也是包括认证和授权，但是oauth是一种协议，只有其中通过密码模式和授权模式才会使用security，而客户端模式和隐式模式都不会）
1.oauth2的基本角色包括：客户端（第三方应用）、授权服务器、资源服务器、用户（资源拥有者）  
2.基本流程就是客户端（第三方应用）请求授权服务器希望授权资源访问权限，用户点击授权后，则客户端（第三方应用）拥有资源访问权限，可以访问资源服务器的资源。  
![img.png](readme-img/oauth2流程.png)  
3.举一个例子：我设计一个网站，希望使用github的用户信息作为登录信息，那么网站就是客户端（第三方应用），github就是授权服务器，github的用户信息就是资源服务器，使用网站的用户就是资源拥有者。具体可以参考auth-github模块  
4.因此如果一套完整的oauth需要有3个服务器，一个是客户端（第三方应用），一个是授权服务器，一个是资源服务器（资源服务器可以与授权服务器是同一台）  
5.oauth2有不同的授权模式，不同方式适用于不同场景  
&ensp;&ensp;1）授权码模式  
&ensp;&ensp;2）简化模式  
&ensp;&ensp;3）密码模式  
&ensp;&ensp;4）客户端模式  
&ensp;&ensp;5）其它模式（设置几种grant_type就可以了，比如说增加SMS（短信认证）类型，指纹认证、人脸认证）

# oauth2提供的令牌端点（Token Endpoint）
/oauth/authorize：授权端点(GET方式返回授权界面,POST方式授权码）  
/oauth/token：令牌端点（返回令牌access_token或者刷新令牌） （前后端分离时可以重写） 
/oauth/confirm_access：用户确认授权提交端点（返回授权界面） （授权码模式下自定义授权界面可以重写） 
/oauth/error：授权服务错误信息端点  
/oauth/check_token：用于资源服务器访问的令牌解析端点  
/oauth/token_key：提供公有密匙的端点，如果使用JWT令牌的话

## 1.auth-authentication基于oauth2的授权服务器
1.使用spring-security集成登录认证，这部分可以参考auth-security模块，这里直接搬过来。  
2.设置配置类AuthorizationServerConfiguration(继承AuthorizationServerConfigurerAdapter)，配置oauth方法
3.这里使用写死客户端注册方式
> 注意：这里写死几种不同的认证模式：authorization_code,refresh_token,client_credentials,password

## 2.演示获得token
### 2.1 授权码模式  
1）关闭postman工具的自动重定向：Automatically follow redirects  
2）通过post方式访问/login接口登录获得token，此处是登录授权服务器的权限认证
![img.png](readme-img/登录.png)
3）通过get方式访问 http://127.0.0.1:8080/oauth/authorize 获得授权界面的授权form（注意：要将第2步的token放入header中）
> **param内容**：  
> client_id=注册客户端的id
> response_type=code  
> redirect_uri=注册客户端的uri
>  
> **header内容**：  
> token=第2步获得的token

![img.png](readme-img/授权.png)
4）通过post方式访问 http://127.0.0.1:8080/oauth/authorize 提交第3步的表单，从而获得code（code在返回的header的Location中）
> **body内容(form-data格式)**：  
> user_oauth_approval=true  
> scope.all=true  
> authorize=Authorize
>
> **header内容**：  
> token=第2步获得的token

![img.png](readme-img/code.png)
5）通过post方式访问 http://client-lq:secret-lq@localhost:8080/oauth/token 获得access_token
> **param内容**：  
> code=第4步获得的code  
> grant_type=authorization_code  
> redirect_uri=注册客户端的uri  
> client_id=注册客户端的id  
> client_secret=注册客户端的secret

![img.png](readme-img/access_token.jpg)

6）获得该access_token即可访问资源服务器

### 2.2 密码模式
1）直接访问 http://localhost:8080/oauth/token
> **param内容**：  
> grant_type=password  
> redirect_uri=注册客户端的uri  
> client_id=注册客户端的id  
> client_secret=注册客户端的secret  
> username=security的用户  
> password=security的密码  

![img.png](readme-img/密码模式.png)
2）直接得到token
> 注意，param的client_id和client_secret参数也可以放到Authorization里面

### 2.3 客户端模式
1）直接访问 http://localhost:8080/oauth/token  
![img.png](readme-img/客户端模式.jpg)
2）直接得到token

## 3.客户端注册存入数据库
1.创建一个表以及对应的Mapper类、entity类
```roomsql
create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);

INSERT INTO commonframework.oauth_client_details
(client_id, resource_ids, client_secret, `scope`, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove)
VALUES('client-lq', 'auth-resource1', 'secret-lq', 'all', 'authorization_code,refresh_token', 'https://www.baidu.com', NULL, 0, 0, NULL, 'true');

```
2.新建ClientDetailsServiceImpl(实现ClientDetailsService接口)，实现loadClientByClientId方法，就是查找用户数据  
3.在AuthorizationServerConfiguration配置的configure(ClientDetailsServiceConfigurer clients)方法，设置内容客户端的service  
**注意：表格字段说明**  
> **client_id**:用于唯一标识每一个客户端(client)；注册时必须填写(也可以服务端自动生成)，这个字段是必须的  
> **resource_ids**：客户端能访问的资源id集合，也是资源服务器id。可以在资源服务器的yml配置security.oauth2.resource.id或者在ResourceServerConfigurerAdapter的configure(ResourceServerSecurityConfigurer resources)方法配置  
> **client_secret**：注册填写或者服务端自动生成，实际应用也有叫app_secret, 必须要有前缀代表加密方式  
> **scope**：指定client的权限范围，比如读写权限，比如移动端还是web端权限	read,write / web,mobile等等，一般在资源服务器配置，在ResourceServerConfigurerAdapter的configure(HttpSecurity http)中设置
> **authorized_grant_types**：可选值 授权码模式:authorization_code,密码模式:password,刷新token: refresh_token, 隐式模式: implicit: 客户端模式: client_credentials。支持多个用逗号分隔  
> **web_server_redirect_uri**：客户端重定向uri，authorization_code和implicit需要该值进行校验，注册时填写  
> **authorities**：指定用户的权限范围，如果授权的过程需要用户登陆，该字段不生效，implicit和client_credentials需要。在资源服务器配置@PreAuthorize("hasAuthority('test')")，某个方法之前  
> **access_token_validity**：设置access_token的有效时间(秒),默认(606012,12小时)   
> **refresh_token_validity**：设置refresh_token有效期(秒)，默认(606024*30, 30填)  
> **additional_information**：值必须是json格式，用于增加信息  
> **autoapprove**：false/true/read/write，默认false,适用于authorization_code模式,设置用户是否自动approval操作,设置true跳过用户确认授权操作页面，直接跳到redirect_uri

4.**(重要)oauth三级权限配置说明**  
1）第一级，资源级别：如果存在多资源，则设置resource_ids，这样每个资源都会有自己的资源id  
2）第二级：读写多端级别：如果资源内部存在读、写、多端等情况，可以设置scope来区分权限，如果没有则设置all  
3）第三级：方法级别：如果有必要对方法级别配置，则配置authorities，否则不做配置  

## 4.授权界面重写
这里有2中方法重写授权页面  
1）重写WhitelabelApprovalEndpoint，重写/oauth/confirm_access接口  
2）重新自己写一个controller接口，可以自定义，然后再在AuthorizationServerConfiguration配置的configure(AuthorizationServerEndpointsConfigurer endpoints)增加endpoints.pathMapping("/oauth/confirm_access","自定义路径");  
**以上2种方式都有设置@SessionAttributes("authorizationRequest")注解**  
下面以第2种方式设置一个自己的登录页面  
1）引入thymeleaf包
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```
2）配置thymeleaf的配置
```yaml
spring:
    thymeleaf:
      prefix: classpath:/static/
      suffix: .html
      cache: false
      mvc:
        throw-exception-if-no-handler-found: true
```
3）定义一个controller接口，参考AuthController  
4）定义一个html，参考oauth-check.html，放在resources/static下面  
5）在AuthorizationServerConfiguration中的configure(AuthorizationServerEndpointsConfigurer endpoints)加入接口名称

## 5.重新刷新token
1）在数据库oauth_client_details表的authorized_grant_types字段添加上refresh_token认证模式  
2）调用 http://client-lq:secret-lq@localhost:8080/oauth/token 接口即可获得新的token  
![img.png](readme-img/refresh_token.jpg)

## 6.自定义授权模式（手机验证码）
首先要理清楚3个类的作用  
AbstractAuthenticationToken：身份验证令牌的抽象类。一般我们自定义认证对象，都是继承自AbstractAuthenticationToken  
AbstractTokenGranter：TokenGranter是我们授权模式接口，而它的基类是AbstractTokenGranter抽象类，通过继承AbstractTokenGranter类并实现其抽象方法，就可以实现我们自定义的授权模式了  
AuthenticationProvider：身份认证是由AuthenticationProvider实现的，所以我们还需要实现一个自定义AuthenticationProvider  
1）分别定义自己的PhoneCodeAuthenticationToken、PhoneCodeTokenGranter、PhoneCodeAuthenticationProvider  
2）在AuthorizationServerConfigurerAdapter的configure(AuthorizationServerEndpointsConfigurer endpoints)中增加PhoneCodeTokenGranter  
3）WebSecurityConfigurerAdapter中的configure(AuthenticationManagerBuilder auth)增加DaoAuthenticationProvider和PhoneCodeAuthenticationProvider  
4）在数据库的oauth_client_details表的authorized_grant_types字段增加phonecode

请求token演示如下：
![img.png](readme-img/phonecode.png)

## 7.重写/oauth/token方法
在某些情况下，可能需要做些判断或者返回值需要做一些改动，可以重新oauth/token方法
```java
@PostMapping("/token")
public com.demo.common.exception.Result<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
    log.info("login params={}", JSONObject.toJSONString(parameters));
    return loginService.getAccessToken(principal,parameters);
}
```
> 注意：方法的定义参照oauth本身的/oauth/token方法。

## 8.与JWT集成（同时使用RSA加密）
1）JWT是一个token生成方式，包括三部分：头部、body、签名。其中body是返回数据（以base64编码），签名是作为认证的。（在auth-security中也使用过）  
2）使用RSA非对称加密对签名进行加密，授权服务器通过私钥加密，资源管理器（也可以是网关）通过公钥解密，这样就能达到签名无法伪造，同时资源管理器验证token不用每次都调用授权服务器  
3）使用RSA加密，需要生成密钥（包括公钥和私钥），以下是生成密钥方法
> ①生成jks
> keytool -genkeypair -alias demo -keyalg RSA -keypass linmoo -storepass linmoo -keysize 2048 -keystore demo.jks  
> 这时候会在目录下生成一个demo.jks文件，该文件包括私钥和公钥 。该文件拷贝到项目resources文件下面，供签名使用
> ![img.png](readme-img/生成RAS密钥.png)
> ②keytool -list -rfc --keystore demo.jks | openssl x509 -inform pem -pubkey
> ![img.png](readme-img/获得公钥.png)
> 将PUBLIC KEY拷贝到一个文件，保存下来，如果资源管理器或者网关要使用，则放在网关，这里给网关使用，所以放在gateway的resources下面

1）配置AccessTokenConfig，配置token生成类（使用JwtAccessTokenConverter）  
2）配置CustomAdditionalInformation，主要是在token的body中增加所需内容，用于前端或者接口可以使用  
3）在AuthorizationServerConfigurerAdapter中增加tokenServices()方法，将AccessTokenConfig和CustomAdditionalInformation注入  
4）在configure(AuthorizationServerEndpointsConfigurer endpoints)方法中注入tokenServices()
> 注意：这个时候还保留security的登录，因为在授权码模式下，还是需要先登录，在获得授权码

## 9.资源服务器鉴权的2种模式
### 9.1 基于check_token接口模式
参考auth-resource子模块，通过yaml文件配置授权服务器的check_token接口，确认token有效性
### 9.2 基于JWT+RSA加密模式
参考gateway子模块，结合JWT+RSA通过对token的签名加密和解密确认token有效性（这样可以减少授权服务器访问）
