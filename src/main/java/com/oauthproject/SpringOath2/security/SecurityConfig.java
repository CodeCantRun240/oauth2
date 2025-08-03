package com.oauthproject.SpringOath2.security;

import com.oauthproject.SpringOath2.service.CustomOauth2Service;

import com.oauthproject.SpringOath2.service.CustomOidcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.oauthproject.SpringOath2.controller.ApiPaths.GET_DETAILS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomOauth2Service customOauth2Service;

    @Autowired
    private CustomOidcService customOidcService;

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository repo) throws Exception {
        CustomAuthorizationRequestResolver resolver =
                new CustomAuthorizationRequestResolver(repo, "/oauth2/authorization");
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/oauth2/**", "/login/**", GET_DETAILS).permitAll()
                        .requestMatchers("/internal/details").authenticated()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestResolver(resolver)
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOauth2Service)
                                .oidcUserService(customOidcService)
                        )
                        .successHandler(customSuccessHandler)
                )
                //
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")

                 )

//                .requiresChannel(channel -> channel
//                        .anyRequest().requiresSecure())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000")); // your frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // must be true to allow cookies like JSESSIONID


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;



    }

}
