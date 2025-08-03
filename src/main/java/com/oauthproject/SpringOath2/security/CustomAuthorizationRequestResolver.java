package com.oauthproject.SpringOath2.security;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.HashMap;
import java.util.Map;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo, String baseUri) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, baseUri);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        // Fallback to the client ID extracted by DefaultOAuth2AuthorizationRequestResolver
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
        return customizeIfFacebook(request, authorizationRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
        return customizeIfFacebook(clientRegistrationId, authorizationRequest);
    }

    private OAuth2AuthorizationRequest customizeIfFacebook(HttpServletRequest request, OAuth2AuthorizationRequest authRequest) {
        if (authRequest == null) return null;
        String uri = request.getRequestURI();
        if (uri.contains("facebook")) {
            return customize(authRequest);
        }
        return authRequest;
    }

    private OAuth2AuthorizationRequest customizeIfFacebook(String clientRegistrationId, OAuth2AuthorizationRequest authRequest) {
        if (authRequest == null) return null;
        if ("facebook".equals(clientRegistrationId)) {
            return customize(authRequest);
        }
        return authRequest;
    }

    private OAuth2AuthorizationRequest customize(OAuth2AuthorizationRequest request) {
        Map<String, Object> additionalParams = new HashMap<>(request.getAdditionalParameters());
        additionalParams.put("auth_type", "reauthenticate"); // Trigger Facebook to prompt account selection

        return OAuth2AuthorizationRequest.from(request)
                .additionalParameters(additionalParams)
                .build();
    }
}
