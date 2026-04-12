package com.example.app.roomReading.presentation.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record RoomReadingResponse(
    Long id,
    Instant createdAt,
    BigDecimal temperature,
    BigDecimal humidity,
    BigDecimal co2,
    String memo) {}
