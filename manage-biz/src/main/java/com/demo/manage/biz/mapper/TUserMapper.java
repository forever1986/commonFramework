package com.demo.manage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.manage.biz.entity.TUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
//@InterceptorIgnore(tenantLine = "true") //使用该注解可以忽略多租户
public interface TUserMapper extends BaseMapper<TUser> {

    // 根据用户名，查询用户信息
    @Select("select * from t_user where username = #{username}")
    TUser selectByUsername(String username);

}
