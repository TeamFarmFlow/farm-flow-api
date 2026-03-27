package com.example.app.farmUser.presentation.response;

public record FarmUserRoleUpdateResponse(
    Long userId, String userName, String roleName, Long roleId) {}
