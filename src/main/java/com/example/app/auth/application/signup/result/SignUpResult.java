package com.example.app.auth.application.signup.result;

import java.time.Instant;

public record SignUpResult(
    String refreshToken,
    String accessToken,
    Instant expiresAt,
    long expiresIn,
    SignUpUserResult user) {
}
