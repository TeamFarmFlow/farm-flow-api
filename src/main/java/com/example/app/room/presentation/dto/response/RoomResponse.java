package com.example.app.room.presentation.dto.response;

import com.example.app.room.domain.enums.RoomStatus;
import java.time.Instant;

public record RoomResponse(
    Long id,
    String name,
    String description,
    RoomStatus status,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt) {}
