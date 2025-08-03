package com.oauthproject.SpringOath2.service;

import com.oauthproject.SpringOath2.enums.AuthProvider;
import com.oauthproject.SpringOath2.model.CustomOauthUser;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;


@Service
public class CustomOidcService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final OidcUserService delegate = new OidcUserService();
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = new OidcUserService().loadUser(userRequest);

        return new CustomOauthUser(
                oidcUser.getAttribute("sub"),
                oidcUser.getEmail(),
                oidcUser.getFullName(),
                userRequest.getClientRegistration().getRegistrationId(),
                oidcUser.getAuthorities(),
                oidcUser.getAttributes()

        );
    }
}

