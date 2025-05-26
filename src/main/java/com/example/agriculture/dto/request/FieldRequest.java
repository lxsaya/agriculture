package com.example.agriculture.dto.request;

import jakarta.validation.constraints.NotNull;

public record FieldRequest(
        @NotNull
        String name,
        @NotNull
        Double area,
        @NotNull
        String soilType
) {}
