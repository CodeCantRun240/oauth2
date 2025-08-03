package com.oauthproject.SpringOath2.controller;

public class ApiPaths {
    // For testing only
    public static final String HOME_GOOGLE = "/home/google";
    public static final String HOME_FACEBOOK = "/home/facebook";
    public static final String USER_INFO = "/user-info";
    public static final String TOKEN = "/token";

    // Auth-related Endpoints
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";

    // Token & User Info Endpoints

    public static final String INTERNAL_TOKEN = "/internal/token";
    public static final String USER_DETAILS = "/internal/user-details";
    public static final String GET_DETAILS = "/internal/details";
    public static final String GET_DETAILS_1 = "/internal/details/demo";




    private ApiPaths() {
        // Prevent instantiation
    }
}
