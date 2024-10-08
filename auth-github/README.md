# auth-github
是一个基于oauth2使用github进行第三方授权认证登录  

## 操作步骤
1.先在github上面注册你的客户端（第三方应用），也就是你的网站。注册地址：https://github.com/settings/applications/new  
2.注册后，你会得到client_id和client_secret。  
3.通过get的方式访问github提供的授权接口：https://github.com/login/oauth/authorize?client_id=注册的clientId&redirect_uri=配置跳转地址  获得一个code  
4.再通过post的方式访问github提供的token接口：https://github.com/login/oauth/access_token"?client_id=注册的clientId&client_secret=密钥&code=code 获得token  
5.再通过get方式访问github资源服务器提供的用户接口：https://api.github.com/user 获得用户信息
