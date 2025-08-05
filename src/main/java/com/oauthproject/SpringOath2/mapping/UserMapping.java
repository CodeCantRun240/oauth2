package com.oauthproject.SpringOath2.mapping;

import com.oauthproject.SpringOath2.dto.UserDTO;
import com.oauthproject.SpringOath2.model.CustomOauthUser;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapping {
    //UserMapping INSTANCE = Mappers.getMapper(UserMapping.class);
    UserDTO toDto(CustomOauthUser customUser);

}
