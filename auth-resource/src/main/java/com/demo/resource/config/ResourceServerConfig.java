//package com.demo.resource.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
//import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
//
//@Configuration
//@EnableResourceServer
////@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别权限控制
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//    @Autowired
//    private RemoteTokenServices remoteTokenServices;
//
//    public static final String RESOURCE_ID = "auth-resource1";
//
//    /**
//     * 设置资源服务器的resourceId，对应注册在授权服务器字段resource_ids
//     * @param resources
//     * @throws Exception
//     */
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        // 当前资源服务器的资源id，认证服务会认证客户端有没有访问这个资源id的权限，有则可以访问当前服务
//        resources.resourceId(RESOURCE_ID)
//                .tokenServices(tokenService())
//        ;
//    }
//
//    /**
//     * 设置远程授权服务器的地方
//     * @return
//     */
//    public ResourceServerTokenServices tokenService(){
//        // 远程认证服务器进行校验 token 是否有效
//        RemoteTokenServices service = new RemoteTokenServices();
//        // 请求认证服务器校验的地址，默认情况 这个地址在认证服务器它是拒绝访问，要设置为认证通过可访问
//        service.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
//        service.setClientId("client-lq");
//        service.setClientSecret("secret-lq");
//        return service;
//    }
//
//    /**
//     * 设置Scope
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.POST).access("#oauth2.hasScope('write')")  // 设置所有POST方法必须拥有write权限
//                .antMatchers(HttpMethod.GET).access("#oauth2.hasScope('read')");   // 设置所有GET方法必须拥有read权限
//    }
//}
