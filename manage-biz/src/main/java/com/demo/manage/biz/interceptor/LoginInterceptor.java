package com.demo.manage.biz.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.demo.common.core.result.ResultCode;
import com.demo.common.exception.BizException;
import com.demo.manage.biz.constant.UserHolder;
import com.demo.manage.biz.entity.TUser;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("=====================用户信息验证=======================");
        // 从请求头获取token
        String token = request.getHeader("Authorization");
        if(!StringUtils.hasText(token)){
            token = request.getHeader("token");
        }
        // 如果不存在token，则返回false
        if (!StringUtils.hasText(token)) {
            throw new BizException(ResultCode.USER_ERROR);
        }
        // 解析token
        try {
            token = StrUtil.replaceIgnoreCase(token, "Bearer ", Strings.EMPTY);
            log.info("user-->"+token);
            String payload = StrUtil.toString(JWSObject.parse(token).getPayload());
            JSONObject jsonObject = JSONUtil.parseObj(payload);
            JSONObject userDetails = (JSONObject)jsonObject.get("userDetails");
            TUser tUser =covert2TUser(userDetails);
            UserHolder.setUserId(tUser);
        } catch (ParseException e) {
            throw new RuntimeException("用户未登录");
        }
        return true;
    }


    public static TUser covert2TUser(JSONObject userDetails){
        JSONObject user = (JSONObject)userDetails.get("user");
        TUser tUser = new TUser();
        tUser.setId(((Integer)user.get("id")).longValue());
        tUser.setUsername((String) user.get("username"));
        tUser.setEmail(user.get("email")==null|| user.get("email") instanceof JSONNull ? null:(String) user.get("email"));
        return tUser;
    }

}
