package com.example.app.attendance.presentation.dto.response;

import com.example.app.attendance.domain.enums.AttendanceStatus;
import java.time.Instant;
import java.time.LocalDate;

public record AttendanceResponse(
    Long id,
    Long userId,
    String userName,
    LocalDate workDate,
    Instant clockInAt,
    Instant clockOutAt,
    AttendanceStatus status) {}
