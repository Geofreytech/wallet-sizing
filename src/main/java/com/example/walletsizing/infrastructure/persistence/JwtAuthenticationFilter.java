package com.example.walletsizing.infrastructure.persistence;

import com.example.walletsizing.infrastructure.persistence.config.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("RAW TOKEN DETECTED: '" + token + "'");
            try {
                String username = jwtUtils.getUsernameFromToken(token);
                String role = jwtUtils.getRoleFromToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // DEBUG: Verify exactly what is being extracted
                    System.out.println("FILTER DEBUG: User [" + username + "] has Role [" + role + "]");

                    // Ensure role isn't null/empty before creating authority
                    String authorityName = (role != null && !role.startsWith("ROLE_")) ? "ROLE_" + role : role;

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(authorityName))
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                // This captures the MalformedJwtException we saw earlier
                System.err.println("JWT Validation Error: " + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
    }
