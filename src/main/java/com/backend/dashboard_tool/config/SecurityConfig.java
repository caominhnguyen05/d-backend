package com.backend.dashboard_tool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import com.backend.dashboard_tool.security.JwtTokenUtil;

/**
 * Security configuration for the application.
 * This class configures the security settings for the application, including
 * CSRF protection, session management, and authorization rules.
 * It also sets up the JWT decoder for OAuth2 resource server support.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Configures the security filter chain for the application.
     * 
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/login").permitAll()
                .anyRequest().permitAll() //TODO this should be changed to authenticated() later on
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder()))
            );
            
        return http.build();
    }
    
    /**
     * Creates a JwtDecoder bean for decoding JWT tokens.
     * This method uses the secret key from the JwtTokenUtil class to create the JwtDecoder.
     * @return the JwtDecoder bean
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(JwtTokenUtil.getSecretKey()).build();
    }
}