package com.example.app.auth.application.result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SignUpResult {
    private String accessToken;
    private Integer expiresIn;
    private LocalDateTime expiredAt;
    private SignUpUserResult user;
}
