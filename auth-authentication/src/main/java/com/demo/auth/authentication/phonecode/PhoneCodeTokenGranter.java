package com.demo.auth.authentication.phonecode;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 手机验证码授权方式定义的tokenGranter
 */
public class PhoneCodeTokenGranter extends AbstractTokenGranter {

    //授权类型名称
    private static final String GRANT_TYPE = "phonecode";

    private final AuthenticationManager authenticationManager;

    /**
     * 构造函数
     * @param tokenServices
     * @param clientDetailsService
     * @param requestFactory
     * @param authenticationManager
     */
    public PhoneCodeTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, AuthenticationManager authenticationManager) {
        this(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE,authenticationManager);
    }

    public PhoneCodeTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType, AuthenticationManager authenticationManager) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        //获取参数
        String phone = parameters.get("phone");
        String phonecode = parameters.get("phonecode");
        //创建未认证对象
        Authentication userAuth = new PhoneCodeAuthenticationToken(phone, phonecode);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            //进行身份认证
            userAuth = authenticationManager.authenticate(userAuth);
        }
        catch (AccountStatusException ase) {
            //将过期、锁定、禁用的异常统一转换
            throw new InvalidGrantException(ase.getMessage());
        }
        catch (BadCredentialsException e) {
            // 用户名/密码错误，我们应该发送400/invalid grant
            throw new InvalidGrantException(e.getMessage());
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("用户认证失败: " + phone);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
