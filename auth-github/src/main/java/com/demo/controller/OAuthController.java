package com.demo.controller;

import com.demo.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class OAuthController {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @GetMapping("/oauth/redirect")
    public String handleRedirect(@RequestParam("code") String requestToken, Model model) {
        // 使用RestTemplate来发送HTTP请求
        RestTemplate restTemplate = new RestTemplate();

        // 获取Token的Url
        String tokenUrl = "https://github.com/login/oauth/access_token" +
                "?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code=" + requestToken;

        // 使用restTemplate向GitHub发送请求，获取Token
        AccessTokenResponse tokenResponse = restTemplate.postForObject(tokenUrl, null, AccessTokenResponse.class);

        // 从响应体中获取Token数据
        String accessToken = tokenResponse.getAccessToken();

        // 携带Token向GitHub发送请求
        String apiUrl = "https://api.github.com/user";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        model.addAttribute("userData", response.getBody());

        return "welcome";
    }
}
