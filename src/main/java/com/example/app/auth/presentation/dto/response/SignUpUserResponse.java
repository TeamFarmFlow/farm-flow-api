package com.example.app.auth.presentation.dto.response;

import com.example.app.auth.application.signup.SignUpUserResult;

public record SignUpUserResponse(
    Long id,
    String email,
    String name) {

  public static SignUpUserResponse from(SignUpUserResult result) {
    return new SignUpUserResponse(
        result.id(),
        result.email(),
        result.name());
  }
}
