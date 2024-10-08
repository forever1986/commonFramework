package com.demo.auth.authentication.phonecode;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 这是自定义的认证token--phonecode方式
 */
@Getter
@Setter
public class PhoneCodeAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    private Object credentials;

    /**
     * 可以自定义属性
     */
    private String phone;


    /**
     * 创建一个未认证的对象
     *
     * @param principal
     * @param credentials
     */
    public PhoneCodeAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    /**
     * 创建一个已认证对象
     *
     * @param authorities
     * @param principal
     * @param credentials
     */
    public PhoneCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        // 必须使用super，因为我们要重写
        super.setAuthenticated(true);
    }

    /**
     * 不能暴露Authenticated的设置方法，防止直接设置
     *
     * @param isAuthenticated
     * @throws IllegalArgumentException
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    /**
     * 用户凭证，如密码
     *
     * @return
     */
    @Override
    public Object getCredentials() {
        return credentials;
    }

    /**
     * 被认证主体的身份，如果是用户名/密码登录，就是用户名
     *
     * @return
     */
    @Override
    public Object getPrincipal() {
        return principal;
    }
}