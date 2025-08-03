package com.oauthproject.SpringOath2.controller;

import com.oauthproject.SpringOath2.dto.AuthResponseDTO;
import com.oauthproject.SpringOath2.dto.UserDTO;
import com.oauthproject.SpringOath2.mapping.UserMapping;
import com.oauthproject.SpringOath2.model.CustomOauthUser;
import com.oauthproject.SpringOath2.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserInfoController {

    @Autowired
    private UserMapping userMapping;

    private final TokenService tokenService;

    @Autowired
    public UserInfoController(TokenService tokenService, UserMapping userMapping) {
        this.tokenService = tokenService;
        this.userMapping = userMapping;
    }

    @GetMapping(ApiPaths.INTERNAL_TOKEN)
    public ResponseEntity<Map<String, String>> getToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String token = tokenService.getAccessToken(auth);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }


    @GetMapping(ApiPaths.USER_DETAILS)
    public ResponseEntity<?> getCurrentUserInfo(Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomOauthUser customUser) {
            session.setAttribute("user", customUser);


            return ResponseEntity.ok(customUser);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid session or user not authenticated");
    }


    @GetMapping(value = ApiPaths.GET_DETAILS_1, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomOauthUser principal) {
        UserDTO dto = userMapping.toDto(principal);
        return ResponseEntity.ok(dto);
        }

    @GetMapping("/show-token")
    public ResponseEntity<?> showToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else {
            return ResponseEntity.badRequest().body("No JWT token found in Authorization header");
        }
    }

        @GetMapping(value = ApiPaths.GET_DETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<UserDTO> getUserProfile(@AuthenticationPrincipal CustomOauthUser userDetails) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Authentication: " + authentication);
            System.out.println("DTO values: " + userDetails.getId() + ", " + userDetails.getName() + ", " + userDetails.getEmail() + "," + userDetails.getProvider());

            System.out.println("USER DETAILS: " + userDetails);

            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                System.out.println("Principal: " + principal);
            }

            UserDTO dto = new UserDTO(
                    userDetails.getId(),
                    userDetails.getName(),
                    userDetails.getEmail(),
                    userDetails.getProvider()
            );

            return ResponseEntity.ok(dto);
        }




}
