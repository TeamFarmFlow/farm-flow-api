package com.example.app.farm.presentation.dto.response;

import com.example.app.farm.domain.enums.FarmStatus;

import java.time.Instant;

public record FarmListResponse(Long id, String name, FarmStatus status, Instant createdAt) {
}
