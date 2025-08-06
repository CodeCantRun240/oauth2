package com.oauthproject.SpringOath2.security;


import com.oauthproject.SpringOath2.util.UrlResolve;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        System.out.println("Authentication Failed: " + exception.getMessage());

        // Resolve frontendUrl from cookies
        String frontendUrl = UrlResolve.resolveFrontendUrl(request);

        // Redirect to frontend login page with error message
        String redirectUrl = frontendUrl + "/login?error=" + exception.getMessage();

        response.sendRedirect(redirectUrl);
    }
}

