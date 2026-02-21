package com.example.walletsizing.application.usecase.dto;


public record CustomerSearchResponse(
        Long id,
        String name,
        String cif,
        String industry,
        String segment
) {}