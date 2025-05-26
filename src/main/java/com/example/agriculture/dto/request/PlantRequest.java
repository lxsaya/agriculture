package com.example.agriculture.dto.request;

import jakarta.validation.constraints.NotNull;

public record PlantRequest(
        @NotNull
        String name,
        @NotNull
        String type,
        Integer growthPeriod
) {}
