package com.oauthproject.SpringOath2.security;

import com.oauthproject.SpringOath2.controller.ApiPaths;
import com.oauthproject.SpringOath2.model.CustomOauthUser;
import com.oauthproject.SpringOath2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    //private final String FRONTEND_REDIRECT_BASE = "https://639b9dbdb834.ngrok-free.app/token";
    private final String FRONTEND_REDIRECT_BASE = "http://localhost:3000/auth/callback?jwtToken=";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException{

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = authToken.getAuthorizedClientRegistrationId();

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();




        CustomOauthUser oauthUser = (CustomOauthUser) authentication.getPrincipal();

        String id = oauthUser.getId();
        String email = oauthUser.getEmail();
        String name = oauthUser.getName();
        String provider =  oauthUser.getProvider();

        String jwtToken = jwtUtil.generateToken(oauthUser);

        System.out.println("LOGIN GOOD from: " + email + " via " + registrationId);

        // Return JWT and user info in response (e.g., JSON)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = String.format("""
            {
              "token": "%s",
              "id": "%s",
              "email": "%s",
              "name": "%s",
              "provider": "%s",
            }
            """, jwtToken, id, email, name, provider);
        String redirectUrl = FRONTEND_REDIRECT_BASE + jwtToken;

        //response.getWriter().write(json);
        //response.getWriter().flush();
        response.sendRedirect(redirectUrl);


        // Redirect to homepage or custom dashboard
//        if ("google".equals(registrationId)) {
//            response.sendRedirect(ApiPaths.HOME_GOOGLE);
//        } else if ("facebook".equals(registrationId)) {
//            response.sendRedirect(ApiPaths.HOME_FACEBOOK);
//        } else {
//            response.sendRedirect(ApiPaths.TOKEN); // fallback
//        }

    }
}
