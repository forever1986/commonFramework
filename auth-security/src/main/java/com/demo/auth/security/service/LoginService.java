package com.demo.auth.security.service;

import com.demo.auth.security.entity.LoginUser;
import com.demo.auth.security.entity.TUser;
import com.demo.auth.security.utils.JwtUtil;
import com.demo.auth.security.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginService{

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录
     * @param user 用户实体类
     * @return 成功
     */
    public ResponseResult login(TUser user) {
        /** 构造了一个基于用户名和密码的认证请求，并通过AuthenticationManager进行用户认证。 */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 判空
        if (Objects.isNull(authenticate)) {
            return new ResponseResult(403, "用户名或密码有误", null);
        }
        // 从认证成功的Authentication对象中获取principal，将其强制转换为LoginUser类型，进而得到用户账号userAccount。
        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
        String username = loginUser.getUser().getUsername();
        // 通过 Jwt 工具类 使用 userAccount 生成 token
        String jwt = JwtUtil.createJWT(username);
        // 将 authenticate 存入 redis
//        redisCache.setCacheObject("login:" + users, loginUser);
        JwtUtil.userMap.put("login:"+username, loginUser);
        // 将 token 响应给前端
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", jwt);

        return new ResponseResult(200, "登录成功", map);
    }

    /**
     * 退出登录
     * @return 成功
     */
    public ResponseResult logout() {
        // 从 存储权限的集合SecurityContextHolder内将获取到Authentication对象。里面包含已认证的用户信息，权限集合等。
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 从 获取到的对象内 取出 已认证的主体
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 从被认证的主体内取出用户名
        String username = loginUser.getUser().getUsername();
        // 根据键为用户名userAccount删除对应的用户信息
//        redisCache.deleteObject("login:" + userAccount);
        JwtUtil.userMap.remove("login:"+username);
        return new ResponseResult<>(200, "退出成功");
    }
}
