package com.example.app.farm.presentation.dto.response;

import com.example.app.farm.domain.enums.FarmStatus;

import java.time.Instant;
import java.util.List;

public record FarmDetailResponse(Long id, String farmName, FarmStatus farmStatus, Instant createdAt, Instant updatedAt, List<FarmMemberResponse> members) {
}
