package com.example.app.cultivationCycle.presentation.dto.response;

import com.example.app.cultivationCycle.domain.enums.CultivationCycleStatus;
import com.example.app.roomReading.presentation.dto.response.RoomReadingResponse;
import java.time.LocalDate;
import java.util.List;

public record CultivationCycleResponse(
    Long id,
    Long roomId,
    String roomName,
    LocalDate inDate,
    LocalDate thinningDate,
    LocalDate harvestStartDate,
    LocalDate outDate,
    CultivationCycleStatus status,
    String note,
    List<RoomReadingResponse> roomReadings) {}
