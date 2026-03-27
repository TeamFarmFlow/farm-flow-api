package com.example.app.farmUser.application.command;

import com.example.app.farmUser.domain.enums.FarmUserStatus;

public record FarmUserStatusUpdateCommand(FarmUserStatus status) {}
