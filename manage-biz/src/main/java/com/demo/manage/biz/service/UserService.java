package com.demo.manage.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.manage.biz.page.UserPageQuery;
import com.demo.manage.biz.entity.TUser;
import com.demo.manage.biz.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private TUserMapper tUserMapper;

    /**
     * 通过username获取用户
     * @param username
     * @return
     */
    public TUser getTUserByUsername(String username){
        return tUserMapper.selectByUsername(username);
    }


    /**
     * 分页查询
     * @param userPageQuery
     * @return
     */
    public IPage<TUser> listTUser(UserPageQuery userPageQuery){
        IPage<TUser> page = tUserMapper.selectPage(new Page<>(userPageQuery.getPageNum(), userPageQuery.getPageSize()), new QueryWrapper<>());
        return page;
    }

}
