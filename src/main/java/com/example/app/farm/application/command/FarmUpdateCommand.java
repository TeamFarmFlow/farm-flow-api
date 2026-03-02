package com.example.app.farm.application.command;

import com.example.app.farm.domain.enums.FarmStatus;

public record FarmUpdateCommand(String name, FarmStatus status) {}
