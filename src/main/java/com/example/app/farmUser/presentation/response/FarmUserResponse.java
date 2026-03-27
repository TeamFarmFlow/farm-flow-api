package com.example.app.farmUser.presentation.response;

import com.example.app.farmUser.domain.enums.FarmUserStatus;

public record FarmUserResponse(
    Long id, String name, String email, FarmUserStatus status, String roleName, Long roleId) {}
