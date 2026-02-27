package com.example.app.auth.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SignUpResponse {
    private final String accessToken;
    private final int expiresIn;
    private final LocalDateTime expiredAt;
}
