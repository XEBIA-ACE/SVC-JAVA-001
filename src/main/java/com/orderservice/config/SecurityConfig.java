package com.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application.
 * Currently configured with basic security - can be extended with JWT authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/health", "/api/status").permitAll()
                // Swagger/OpenAPI endpoints
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // Actuator endpoints
                .requestMatchers("/actuator/**").permitAll()
                // All other endpoints require authentication
                // TODO: Replace with JWT authentication in production
                .anyRequest().permitAll() // Changed to permitAll for demo purposes
            );

        return http.build();
    }

    // TODO: Add JWT authentication configuration
    // @Bean
    // public JwtDecoder jwtDecoder() {
    //     return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    // }

    // TODO: Add password encoder for user authentication
    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }
}
