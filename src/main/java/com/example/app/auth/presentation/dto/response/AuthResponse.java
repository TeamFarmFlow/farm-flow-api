package com.example.app.auth.presentation.dto.response;

import com.example.app.auth.application.result.AuthResult;
import java.time.Instant;

public record AuthResponse(
    String accessToken, Instant expiresAt, long expiresIn, AuthUserResponse user) {

  public static AuthResponse from(AuthResult result) {
    return new AuthResponse(
        result.accessToken(),
        result.expiresAt(),
        result.expiresIn(),
        AuthUserResponse.from(result.user()));
  }
}
