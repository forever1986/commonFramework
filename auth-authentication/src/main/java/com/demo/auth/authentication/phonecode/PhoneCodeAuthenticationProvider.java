package com.demo.auth.authentication.phonecode;

import com.demo.auth.authentication.service.UserPhoneService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class PhoneCodeAuthenticationProvider implements AuthenticationProvider {


    public static final String PHONE_CODE = "1234";

    private UserPhoneService userPhoneService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //先将authentication转为我们自定义的Authentication对象
        PhoneCodeAuthenticationToken authenticationToken = (PhoneCodeAuthenticationToken) authentication;
        //校验参数
        Object principal = authentication.getPrincipal();
        Object credentials = authentication.getCredentials();
        if (principal == null || "".equals(principal.toString()) || credentials == null || "".equals(credentials.toString())) {
            throw new InternalAuthenticationServiceException("手机/手机验证码为空！");
        }
        //获取手机号和验证码
        String phone = (String) authenticationToken.getPrincipal();
        String code = (String) authenticationToken.getCredentials();
        // TODO: 这里使用写死认证码方式，真实环境可能使用redis存储验证码
        // 通过查找手机用户信息，验证用户是否存在
        UserDetails userDetails = userPhoneService.selectByPhone(phone);
        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("用户手机不存在！");
        }
        //
        if (!PHONE_CODE.equals(code)) {
            throw new InternalAuthenticationServiceException("验证码错误！");
        }
        //返回认证成功的对象
        PhoneCodeAuthenticationToken phoneAuthenticationToken = new PhoneCodeAuthenticationToken(userDetails.getAuthorities(), userDetails.getUsername(), code);
        phoneAuthenticationToken.setPhone(phone);
        //details是一个泛型属性，用于存储关于认证令牌的额外信息。其类型是 Object，所以你可以存储任何类型的数据。这个属性通常用于存储与认证相关的详细信息，比如用户的角色、IP地址、时间戳等。
        phoneAuthenticationToken.setDetails(userDetails);
        return phoneAuthenticationToken;
    }


    /**
     * ProviderManager 选择具体Provider时根据此方法判断
     * 判断 authentication 是不是 SmsCodeAuthenticationToken 的子类或子接口
     */
    @Override
    public boolean supports(Class<?> authentication) {
        //isAssignableFrom方法如果比较类和被比较类类型相同，或者是其子类、实现类，返回true
        return PhoneCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
