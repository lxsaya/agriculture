package com.example.agriculture.dto.responce;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PlantResponse(
        @NotNull
        UUID id,
        @NotNull
        String name,
        @NotNull
        String type,
        Integer growthPeriod
) {}
