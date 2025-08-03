package com.oauthproject.SpringOath2.service;

import com.oauthproject.SpringOath2.model.CustomOauthUser;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;



@Service
public class CustomOauth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        System.out.println("[Facebook] CustomOAuth2UserService called");
        System.out.println("OAuth2 User Attributes:");
        oAuth2User.getAttributes().forEach((key, value) ->
                System.out.println("  " + key + " -> " + value)
        );

        return new CustomOauthUser(
                oAuth2User.getAttribute("id"),
                oAuth2User.getAttribute("email"),
                oAuth2User.getAttribute("name"),
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAuthorities(),
                oAuth2User.getAttributes()
        );
    }
}


