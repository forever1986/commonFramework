package com.demo.auth.authentication.config;

import com.demo.auth.authentication.jwt.CustomAdditionalInformation;
import com.demo.auth.authentication.phonecode.PhoneCodeTokenGranter;
import com.demo.auth.authentication.service.ClientDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableAuthorizationServer  //指定当前应用为授权服务器
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    public AuthorizationServerConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // 用来配置授权服务器可以为哪些客户端授权   client_id,secret  redirect_url
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        // 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者，用于支持原有授权模式
        List<TokenGranter> granterList = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        //添加我们的自定义TokenGranter到集合
        granterList.add(new PhoneCodeTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), manager));
        //CompositeTokenGranter是一个TokenGranter组合类
        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);

        endpoints.authenticationManager(manager)
                .userDetailsService(userDetailsService)
                .tokenServices(tokenServices())
                .tokenGranter(compositeTokenGranter);

        //这个是用于在登陆成功后，将授权请求Action替换自定义的Action，以便进入自定义授权页面
        endpoints.pathMapping("/oauth/confirm_access","/custom/confirm_access");
    }


    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    CustomAdditionalInformation customAdditionalInformation;
    @Autowired
    TokenStore tokenStore;

    @Bean
    AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore);
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        //注意这个顺序，如果custom在jwt前面，则custom新增内容会被加入token进去加密；如果jwt在custom之前，则新增内容不会被放到token中
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customAdditionalInformation,jwtAccessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);
        return services;
    }



}
