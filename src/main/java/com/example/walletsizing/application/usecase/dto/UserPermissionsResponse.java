package com.example.walletsizing.application.usecase.dto;


import java.util.List;

/**
 * A clean DTO to return user capabilities to the frontend.
 */
public record UserPermissionsResponse(
        String username,
        List<String> permissions
) {}