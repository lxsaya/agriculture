package com.example.agriculture.dto.request;

import java.util.UUID;

public record SowingLogRequest(
        UUID workerId,
        UUID plantId,
        UUID fieldId,
        Double quantityKg
) {}
