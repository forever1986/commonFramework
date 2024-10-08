package com.demo.auth.authentication.mapper;

import com.demo.auth.authentication.entity.OauthClientDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OauthClientDetailsMapper {

    @Select("select * from oauth_client_details where client_id = #{clientId}")
    OauthClientDetails selectOauthClient(String clientId);

}
