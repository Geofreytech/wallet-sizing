package com.example.walletsizing.api.controller;

import com.example.walletsizing.application.usecase.dto.UserResponse;
import com.example.walletsizing.infrastructure.persistence.config.JwtUtils;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");

        // FIX: Ensure the variable name matches what is used in the Map below
        String accessToken = jwtUtils.generateToken(username, "RM");
        String refreshToken = jwtUtils.generateRefreshToken(username);

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
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

        return ResponseEntity.ok(new UserResponse(username, role, "Geoffrey Mwangi"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        // Note: Check if your JwtUtils uses getUsernameFromToken or extractUsername
        if (refreshToken != null && jwtUtils.validateToken(refreshToken)) {
            String username = jwtUtils.getUsernameFromToken(refreshToken);

            String newAccessToken = jwtUtils.generateToken(username, "RM");

            return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", refreshToken
            ));
        }

        return ResponseEntity.status(401).body("Invalid Refresh Token");
    }
}