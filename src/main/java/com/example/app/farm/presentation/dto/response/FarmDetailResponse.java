package com.example.app.farm.presentation.dto.response;

import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farmUser.presentation.response.FarmUserResponse;
import java.time.Instant;
import java.util.List;

public record FarmDetailResponse(
    Long id,
    String name,
    FarmStatus status,
    Instant createdAt,
    Instant updatedAt,
    List<FarmUserResponse> members) {}
