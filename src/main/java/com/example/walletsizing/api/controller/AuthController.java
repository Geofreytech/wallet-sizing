package com.example.walletsizing.api.controller;


import com.example.walletsizing.infrastructure.persistence.config.JwtUtils;
import org.springframework.http.ResponseEntity;
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
}