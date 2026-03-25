package com.example.app.farmUser.presentation.response;

import com.example.app.user.domain.enums.UserStatus;

public record FarmUserResponse(Long id, String name, String email, UserStatus status) {}
