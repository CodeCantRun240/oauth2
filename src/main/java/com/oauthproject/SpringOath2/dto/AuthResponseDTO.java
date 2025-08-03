package com.oauthproject.SpringOath2.dto;

public class AuthResponseDTO {
    private String token;
    private UserDTO user;

    public AuthResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    // Getters and setters
}
