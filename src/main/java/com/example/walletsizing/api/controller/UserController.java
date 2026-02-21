package com.example.walletsizing.api.controller;


import com.example.walletsizing.application.usecase.dto.UserPermissionsResponse;
import com.example.walletsizing.application.usecase.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // In a real app, you might fetch full details from a Database here
        // For now, we use the token data
        return ResponseEntity.ok(new UserResponse(
                auth.getName(),
                "RM", // Extracted from authorities
                "Geoffrey Mwangi"
        ));
    }

    @GetMapping("/permissions")
    public ResponseEntity<UserPermissionsResponse> getPermissions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<String> permissions = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UserPermissionsResponse(auth.getName(), permissions));
    }

}