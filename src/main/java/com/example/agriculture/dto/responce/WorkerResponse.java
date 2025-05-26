package com.example.agriculture.dto.responce;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record WorkerResponse(
        @NotNull
        UUID id,
        @NotNull
        String fullName,
        @NotNull
        String position
) {}
