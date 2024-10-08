package com.demo.auth.authentication.mapper;

import com.demo.auth.authentication.entity.TUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TUserMapper {

    // 根据用户名，查询用户信息
    @Select("select * from t_user where username = #{username}")
    TUser selectByUsername(String username);


    // 根据用户手机，查询用户信息
    @Select("select * from t_user where phone = #{phone}")
    TUser selectByPhone(String phone);

}
