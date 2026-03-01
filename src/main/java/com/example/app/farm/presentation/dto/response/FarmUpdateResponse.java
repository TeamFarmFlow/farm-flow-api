package com.example.app.farm.presentation.dto.response;

import com.example.app.farm.domain.enums.FarmStatus;

public record FarmUpdateResponse(Long farmId, Long userId, String farmName, FarmStatus status) {}
