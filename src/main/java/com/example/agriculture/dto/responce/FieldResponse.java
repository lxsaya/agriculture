package com.example.agriculture.dto.responce;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FieldResponse(
        @NotNull
        UUID id,
        @NotNull
        String name,
        @NotNull
        Double area,
        @NotNull
        String soilType
) {}
