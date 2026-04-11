package com.example.app.cultivationCycle.presentation.dto.response;

import com.example.app.cultivationCycle.domain.enums.CultivationCycleStatus;
import java.time.LocalDate;

public record CultivationCycleResponse(
    Long id,
    Long roomId,
    String roomName,
    LocalDate inDate,
    LocalDate thinningDate,
    LocalDate harvestStartDate,
    LocalDate outDate,
    CultivationCycleStatus status,
    String note) {}
