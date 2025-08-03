package com.oauthproject.SpringOath2.security;

import com.oauthproject.SpringOath2.model.CustomOauthUser;
import com.oauthproject.SpringOath2.service.CustomOauth2Service;
import com.oauthproject.SpringOath2.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("JwtAuthFilter: Incoming request - " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Remove "Bearer " prefix
            System.out.println("JwtAuthFilter: JWT found - " + jwt);
        }else {
            System.out.println("JwtAuthFilter: No Authorization header or not Bearer.");
        }

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String email = jwtUtil.extractEmail(jwt);
                if (!jwtUtil.isTokenExpired(jwt)) {
                    // Create a basic CustomOauthUser from JWT claims
                    String id = jwtUtil.extractClaims(jwt).get("id", String.class);
                    String name = jwtUtil.extractClaims(jwt).get("name", String.class);

                    CustomOauthUser customUser = new CustomOauthUser(email, id, name);
                    System.out.println("Setting auth: email=" + email + ", id=" + id + ", name=" + name);


                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (ExpiredJwtException e) {
                // Token is expired
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expired");
                return;
            } catch (MalformedJwtException | IllegalArgumentException e) {
                // Invalid or empty token
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid token");
                return;
            }



        }
        filterChain.doFilter(request, response);
    }
}
