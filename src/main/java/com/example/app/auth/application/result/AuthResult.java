package com.example.app.auth.application.result;

import java.time.Instant;

public record AuthResult(
    String refreshToken, String accessToken, Instant expiresAt, long expiresIn, AuthUser user) {}
