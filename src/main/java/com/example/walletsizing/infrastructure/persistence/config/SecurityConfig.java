package com.example.walletsizing.infrastructure.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (Cross-Site Request Forgery) for development
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Authorize all requests without requiring a password
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // 3. Disable X-Frame-Options to allow H2 console or other tools if needed
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}