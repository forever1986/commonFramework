package com.demo.auth.authentication.config;

import com.alibaba.fastjson.JSON;
import com.demo.auth.authentication.phonecode.PhoneCodeAuthenticationProvider;
import com.demo.auth.authentication.service.LoginService;
import com.demo.auth.authentication.service.UserDetailsServiceImpl;
import com.demo.auth.authentication.service.UserPhoneService;
import com.demo.auth.authentication.utils.ResponseResult;
import com.demo.auth.authentication.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserPhoneService userPhoneService;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 密码加密器
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 这里注册是为了中LoginService使用
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 来配置认证管理器AuthenticationManager。说白了就是所有 UserDetails 相关的它都管，包含 PasswordEncoder 密码等
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(phoneCodeAuthenticationProvider());
        auth.authenticationProvider(daoAuthenticationProvider());//如果要保留原先通过密码登录，则需要增加这个Provider
    }

    /**
     * 用来配置 WebSecurity 。而 WebSecurity 是基于 Servlet Filter 用来配置 springSecurityFilterChain
     * 一般不会过多来自定义 WebSecurity , 使用较多的使其ignoring() 方法用来忽略 Spring Security 对静态资源的控制。
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * 用来配置 HttpSecurity 。 HttpSecurity 用于构建一个安全过滤器链 SecurityFilterChain 。SecurityFilterChain 最终被注入核心过滤器。
     * HttpSecurity 有许多需要的配置。我们可以通过它来进行自定义安全访问策略
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 因为是 前后端分离 要关闭 csrf()
                .csrf().disable()
                // 不通过 session 获取 SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers("/oauth/authorize").permitAll() //鉴权不需要登录
//                .antMatchers("/oauth/token").permitAll()  //获取token不需要登录
                // 登录接口公开访问
                .antMatchers("/login").permitAll()
                .antMatchers("/oauth/token").permitAll()
                // 除上面公开的接口外，所有的请求都需要鉴定认证
                .anyRequest().authenticated();

        // 在 SecurityConfig 内将 token 校验过滤器添加到过滤器链内
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        /**
         * AuthenticationEntryPoint 接口是 Spring Security 中的一个接口，用于处理在用户尝试访问受保护资源时出现的身份验证异常。
         * 它定义了一个方法 commence，该方法在身份验证失败时被调用，允许应用程序自定义处理方式，例如重定向到登录页面、返回特定的错误响应等。
         */
        http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint(){
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "认证失败请重新登录");
                String json = JSON.toJSONString(result);
                ResponseUtils.renderString(response,json);
            }
        });

    }

    @Bean
    public PhoneCodeAuthenticationProvider phoneCodeAuthenticationProvider(){
        //实例化provider，把需要的属性set进去
        PhoneCodeAuthenticationProvider phoneCodeAuthenticationProvider = new PhoneCodeAuthenticationProvider();
        phoneCodeAuthenticationProvider.setUserPhoneService(userPhoneService);
        return phoneCodeAuthenticationProvider;
    }

    /**
     * 如果要保留原先通过密码登录，则需要增加这个Provider
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        // 是否隐藏用户不存在异常，默认:true-隐藏；false-抛出异常；
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

}
