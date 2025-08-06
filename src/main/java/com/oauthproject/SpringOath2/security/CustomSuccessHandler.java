package com.oauthproject.SpringOath2.security;

import com.oauthproject.SpringOath2.model.CustomOauthUser;
import com.oauthproject.SpringOath2.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;


@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    //private final String FRONTEND_REDIRECT_BASE = getFrontendUrl() + "/auth/callback?jwtToken=";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException{
        CustomOauthUser oauthUser = (CustomOauthUser) authentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(oauthUser);

        System.out.println("LOGIN SUCCESS: " + oauthUser.getEmail() + " via " + oauthUser.getProvider());

// Set JWT as HttpOnly Cookie
        ResponseCookie cookie = ResponseCookie.from("jwtToken", jwtToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        Cookie[] cookies = request.getCookies();
        String frontendUrl = "http://localhost:4040"; // fallback
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("frontendUrl".equals(c.getName())) {
                    frontendUrl = c.getValue();
                    break;
                }
            }
        }
        response.sendRedirect(frontendUrl + "/auth/callback");



    }
}
