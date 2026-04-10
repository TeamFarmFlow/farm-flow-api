package com.example.app.room.application.command;

import com.example.app.room.domain.enums.RoomStatus;

public record RoomUpdateCommand(String name, String description, RoomStatus status) {}
