package com.oauthproject.SpringOath2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    public String getAccessToken(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
            throw new IllegalArgumentException("Not an authenticated user");
        }

        String registrationId = oauthToken.getAuthorizedClientRegistrationId();

        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(registrationId, oauthToken.getName());

        return client.getAccessToken().getTokenValue();
    }
}

