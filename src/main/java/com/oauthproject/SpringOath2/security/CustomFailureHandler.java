package com.oauthproject.SpringOath2.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;



@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {
    @Value("${frontend.url}")
    private String frontendUrl;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        System.out.println("Authentication Failed: " + exception.getMessage());

        // Redirect to frontend login page with error message
        String redirectUrl = frontendUrl + exception.getMessage();

        response.sendRedirect(redirectUrl);
    }
}
