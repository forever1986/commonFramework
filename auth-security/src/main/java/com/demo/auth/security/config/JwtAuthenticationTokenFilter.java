package com.demo.auth.security.config;

import com.demo.auth.security.entity.LoginUser;
import com.demo.auth.security.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 用于集成JWT认证
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头获取token
        String token = request.getHeader("token");
        // 检查获取到的token是否为空或空白字符串。(判断给定的字符串是否包含文本)
        if (!StringUtils.hasText(token)) {
            // 如果token为空，则直接放行请求到下一个过滤器，不做进一步处理并结束当前方法，不继续执行下面代码。
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        String userAccount;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userAccount = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        String redisKey = "login:" + userAccount;
        // 临时缓存中 获取 键 对应 数据
        LoginUser loginUser = JwtUtil.userMap.get(redisKey);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("用户未登录");
        }
        // 将用户信息存入 SecurityConText
        // UsernamePasswordAuthenticationToken 存储用户名 密码 权限的集合
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        // SecurityContextHolder是Spring Security用来存储当前线程安全的认证信息的容器。
        // 将用户名 密码 权限的集合存入SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }
}
