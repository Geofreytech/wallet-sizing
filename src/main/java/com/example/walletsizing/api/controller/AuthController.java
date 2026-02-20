package com.example.walletsizing.api.controller;


import com.example.walletsizing.application.usecase.dto.UserResponse;
import com.example.walletsizing.infrastructure.persistence.config.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUtils jwtUtils;

    public AuthController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * MOCK LOGIN: In production, this would validate against Active Directory.
     * For now, it just returns a signed JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");

        // Simulating RBAC from SDD Section 6.2
        // We will issue an "RM" role to anyone who logs in for testing
        String token = jwtUtils.generateToken(username, "RM");

        return ResponseEntity.ok(Map.of(
                "accessToken", token,
                "tokenType", "Bearer",
                "role", "RM"
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = auth.getName();
        String role = auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .findFirst()
                .orElse("ROLE_USER");

        // Ensure you are passing exactly 3 strings to match the record above
        return ResponseEntity.ok(new UserResponse(username, role, "Geoffrey Mwangi"));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Clear the security context
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}