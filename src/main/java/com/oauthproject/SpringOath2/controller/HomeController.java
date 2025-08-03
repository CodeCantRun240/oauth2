package com.oauthproject.SpringOath2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping(ApiPaths.HOME_GOOGLE)
    public String homeGoogle(OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();

        System.out.println("Google user attributes:");
        user.getAttributes().forEach((k, v) -> System.out.println(k + ": " + v));

        String id = user.getAttribute("sub");
        String name = user.getAttribute("name");
        String email = user.getAttribute("email");
        String firstName = user.getAttribute("given_name");
        String lastName = user.getAttribute("family_name");
        String locale = user.getAttribute("locale");
        return """
        <html>
            <body>
                <h1>Welcome, %s!</h1>
                <p>Id: %s</p>
                <p>Email: %s</p>
                <p>First Name: %s</p>
                <p>Last Name: %s</p>
                <p>Locale: %s</p>
       
            </body>
        </html>
        """.formatted(name, id, email, firstName, lastName, locale);
    }

    @GetMapping(ApiPaths.HOME_FACEBOOK)
    public String homeFacebook(OAuth2AuthenticationToken authentication) {

        OAuth2User user = authentication.getPrincipal();

        System.out.println("Facebook user attributes:");
        user.getAttributes().forEach((k, v) -> System.out.println(k + ": " + v));

        String id = user.getAttribute("id");
        String name = user.getAttribute("name");
        String email = user.getAttribute("email");
        String firstName = user.getAttribute("first_name");
        String lastName = user.getAttribute("last_name");
        String locale = user.getAttribute("locale");
        return """
        <html>
            <body>
                <h1>Welcome, %s!</h1>
                <p>Id: %s</p>
                <p>Email: %s</p>
                <p>First Name: %s</p>
                <p>Last Name: %s</p>
                <p>Locale: %s</p>
        
            </body>
        </html>
        """.formatted(name, id, email, firstName, lastName, locale);

    }

    @GetMapping(ApiPaths.TOKEN)
    public String token(OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();

        // Get the authorized client (which holds the tokens)
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();
        String idToken = user.getAttribute("id_token"); // Sometimes present depending on configuration

        return "Access Token: " + accessToken;
    }

    @GetMapping(ApiPaths.USER_INFO)
    public ResponseEntity<Map<String, Object>> userInfo(@AuthenticationPrincipal OAuth2User user) {
        return ResponseEntity.ok(user.getAttributes());
    }

}
