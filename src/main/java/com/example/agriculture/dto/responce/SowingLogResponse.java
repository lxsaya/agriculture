package com.example.agriculture.dto.responce;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record SowingLogResponse(
        @NotNull
        UUID id,
        LocalDate sowingDate,
        UUID workerId,
        UUID plantId,
        UUID fieldId,
        Boolean isFieldSown,
        Double quantityKg
) {}
