package com.example.agriculture.dto.request;

import jakarta.validation.constraints.NotNull;

public record WorkerRequest(
        @NotNull
        String fullName,
        @NotNull
        String position
) {}
