package com.example.app.auth.presentation.dto.response;

import com.example.app.auth.application.result.AuthUser;

public record AuthUserResponse(Long id, String email, String name) {
  public static AuthUserResponse from(AuthUser result) {
    return new AuthUserResponse(result.id(), result.email(), result.name());
  }
}
