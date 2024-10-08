package com.demo.auth.security.service;

import com.demo.auth.security.entity.LoginUser;
import com.demo.auth.security.entity.TUser;
import com.demo.auth.security.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询自己数据库的用户信息
        TUser user = tUserMapper.selectByUsername(username);
        return new LoginUser(user);
    }

}
