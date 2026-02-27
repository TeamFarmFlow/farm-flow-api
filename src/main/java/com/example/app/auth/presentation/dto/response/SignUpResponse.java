package com.example.app.auth.presentation.dto.response;

import com.example.app.auth.application.signup.result.SignUpResult;
import java.time.Instant;

public record SignUpResponse(
    String accessToken, Instant expiresAt, long expiresIn, SignUpUserResponse user) {

  public static SignUpResponse from(SignUpResult result) {
    return new SignUpResponse(
        result.accessToken(),
        result.expiresAt(),
        result.expiresIn(),
        SignUpUserResponse.from(result.user()));
  }
}
