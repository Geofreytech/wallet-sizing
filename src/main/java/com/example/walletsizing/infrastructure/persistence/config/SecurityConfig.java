package com.example.walletsizing.infrastructure.persistence.config;

import com.example.walletsizing.infrastructure.persistence.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. PUBLIC: Only Login needs to be open to everyone
                        .requestMatchers("/api/v1/auth/login").permitAll()

                        .requestMatchers("/api/v1/auth/refresh").permitAll() // Permit so users with expired access tokens can call it

                        // 2. PRIVATE: These auth endpoints need a valid JWT
                        .requestMatchers("/api/v1/auth/me", "/api/v1/auth/logout", "/api/v1/auth/refresh").authenticated()

                        // Keep your existing error and customer rules
                        .requestMatchers("/error").permitAll()
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/api/v1/customers/**").hasAnyAuthority("ROLE_RM", "ROLE_LEAD")

                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}