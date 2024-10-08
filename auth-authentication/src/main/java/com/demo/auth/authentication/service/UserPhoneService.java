package com.demo.auth.authentication.service;

import com.demo.auth.authentication.entity.LoginUser;
import com.demo.auth.authentication.entity.TUser;
import com.demo.auth.authentication.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPhoneService {

    @Autowired
    private TUserMapper tUserMapper;

    public LoginUser selectByPhone(String phone){
        // 查询自己数据库的用户信息
        TUser user = tUserMapper.selectByPhone(phone);
        if(user==null){
            return null;
        }
        return new LoginUser(user);
    }
}
