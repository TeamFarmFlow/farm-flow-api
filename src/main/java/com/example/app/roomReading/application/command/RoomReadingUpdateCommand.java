package com.example.app.roomReading.application.command;

import java.math.BigDecimal;

public record RoomReadingUpdateCommand(
    Long cultivationCycle_id,
    BigDecimal temperature,
    BigDecimal humidity,
    BigDecimal co2,
    String memo) {}
