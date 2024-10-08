package com.demo.auth.authentication.controller;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@SessionAttributes("authorizationRequest")
public class AuthController {

    @RequestMapping("/custom/confirm_access")
    public String getAccessConfirmation(Map<String, Object> param, HttpServletRequest request, Model model) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) param.get("authorizationRequest");

        String clientId = authorizationRequest.getClientId();
        model.addAttribute("scopes",authorizationRequest.getScope());
        Map<String, Object> client = new HashMap<>();
        client.put("clientId",clientId);
        client.put("name","lin");  // 可以设置用户名
        model.addAttribute("client",client);
        return "oauth-check";
    }

}
