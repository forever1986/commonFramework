package com.demo.route.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
public class ResourceServerManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    public final String AUTHORIZATION_KEY ="Authorization";

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        // 预检请求放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }
        // 判断token（必须在Authorization中有）
        String token = StringUtils.EMPTY;
        boolean tokenCheckFlag = false;
        if(Objects.nonNull(request.getHeaders().getFirst(AUTHORIZATION_KEY)) && StringUtils.isNotBlank(request.getHeaders().getFirst(AUTHORIZATION_KEY))){
            token = request.getHeaders().getFirst(AUTHORIZATION_KEY);
            tokenCheckFlag = true;
        }
        if (!tokenCheckFlag) {
            return Mono.just(new AuthorizationDecision(false));
        }
        log.info("判断权限");
        mono.filter(authentication -> {
            log.info("权限"+String.valueOf(authentication.isAuthenticated()));
            return authentication.isAuthenticated();
        });
        return Mono.just(new AuthorizationDecision(true));
    }
}
