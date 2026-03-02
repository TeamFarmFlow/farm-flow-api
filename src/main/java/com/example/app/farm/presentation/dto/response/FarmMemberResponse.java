package com.example.app.farm.presentation.dto.response;

import com.example.app.user.domain.enums.UserStatus;

public record FarmMemberResponse(Long userId, String userName, String email, UserStatus userStatus) {
}
