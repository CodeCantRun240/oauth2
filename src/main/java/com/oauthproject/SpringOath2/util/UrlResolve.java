package com.oauthproject.SpringOath2.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class UrlResolve {

    public static String resolveFrontendUrl(HttpServletRequest request) {
        String DEFAULT_FRONTEND_URL = "http://localhost:4040";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("frontendUrl".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return DEFAULT_FRONTEND_URL;
    }

}
