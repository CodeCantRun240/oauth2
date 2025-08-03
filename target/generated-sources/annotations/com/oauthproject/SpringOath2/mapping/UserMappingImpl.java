package com.oauthproject.SpringOath2.mapping;

import com.oauthproject.SpringOath2.dto.AuthResponseDTO;
import com.oauthproject.SpringOath2.dto.UserDTO;
import com.oauthproject.SpringOath2.model.AuthResponse;
import com.oauthproject.SpringOath2.model.CustomOauthUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-31T17:58:51+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class UserMappingImpl implements UserMapping {

    @Override
    public UserDTO toDto(CustomOauthUser customUser) {
        if ( customUser == null ) {
            return null;
        }

        String id = null;
        String provider = null;
        String name = null;
        String email = null;

        id = customUser.getId();
        provider = customUser.getProvider();
        name = customUser.getName();
        email = customUser.getEmail();

        UserDTO userDTO = new UserDTO( id, name, email, provider );

        return userDTO;
    }

    @Override
    public AuthResponseDTO toAuthResponse(AuthResponse authResponse) {
        if ( authResponse == null ) {
            return null;
        }

        String token = null;
        UserDTO user = null;

        token = authResponse.getToken();
        user = authResponse.getUser();

        AuthResponseDTO authResponseDTO = new AuthResponseDTO( token, user );

        return authResponseDTO;
    }
}
