package com.example.app.farm.presentation.dto.response;

import com.example.app.farm.domain.enums.FarmStatus;

public record FarmUpdateResponse(Long id, String name, FarmStatus status) {}
