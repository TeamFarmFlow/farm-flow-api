package com.example.app.attendance.application.command;

import java.time.LocalDateTime;

public record AttendanceUpdateCommand(
    LocalDateTime clockInAt, LocalDateTime clockOutAt, String reason) {}
