package com.demo.auth.authentication.service;

import com.demo.auth.authentication.entity.LoginUser;
import com.demo.auth.authentication.entity.TUser;
import com.demo.auth.authentication.mapper.TUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("load username="+ username);
        // 查询自己数据库的用户信息
        TUser user = tUserMapper.selectByUsername(username);
        return new LoginUser(user);
    }

}
