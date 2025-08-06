package com.oauthproject.SpringOath2.controller;

import com.oauthproject.SpringOath2.util.UrlResolve;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class LogoutController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;



//    @GetMapping(ApiPaths.LOGOUT)
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        // Clear Spring Security context
//        SecurityContextHolder.clearContext();
//
//        // Invalidate session
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//
//
//        // Redirect to your login page
//        return "redirect:" + ApiPaths.LOGIN;
//    }

    @GetMapping(ApiPaths.LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException  {

        String frontendUrl = UrlResolve.resolveFrontendUrl(request);

        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Ensure HTTPS in production
        cookie.setMaxAge(0); // Delete cookie
        response.addCookie(cookie);

        System.out.println("User logged out, JWT cookie deleted.");

        // Optionally redirect frontend after logout
        String redirectUrl = frontendUrl;

        response.sendRedirect(redirectUrl);
        return null;
    }
}