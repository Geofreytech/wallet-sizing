package com.example.walletsizing.application.usecase.dto;


import java.util.Set;

public record UserResponse(
        String username,
        String role,
        String fullName // You can add this if it's in your User entity
) {}