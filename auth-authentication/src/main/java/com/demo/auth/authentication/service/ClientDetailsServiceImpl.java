package com.demo.auth.authentication.service;

import com.demo.auth.authentication.entity.OauthClientDetails;
import com.demo.auth.authentication.mapper.OauthClientDetailsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.info("load client="+ clientId);
        OauthClientDetails oauthClientDetails = oauthClientDetailsMapper.selectOauthClient(clientId);

        if(Objects.isNull(oauthClientDetails)){
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
        BaseClientDetails clientDetails = new BaseClientDetails(
                oauthClientDetails.getClientId(),
                oauthClientDetails.getResourceIds(),
                oauthClientDetails.getScope(),
                oauthClientDetails.getAuthorizedGrantTypes(),
                oauthClientDetails.getAuthorities(),
                oauthClientDetails.getWebServerRedirectUri()
        );
        clientDetails.setClientSecret(oauthClientDetails.getClientSecret());//看看是否加密
        clientDetails.setAccessTokenValiditySeconds(oauthClientDetails.getAccessTokenValidity());
        clientDetails.setRefreshTokenValiditySeconds(oauthClientDetails.getRefreshTokenValidity());
        return clientDetails;
    }

}
