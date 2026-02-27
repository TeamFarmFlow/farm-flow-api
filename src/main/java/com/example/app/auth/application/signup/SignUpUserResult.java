package com.example.app.auth.application.signup;

import com.example.app.user.domain.User;

public record SignUpUserResult(
    Long id,
    String email,
    String name) {
  public static SignUpUserResult from(User user) {
    return new SignUpUserResult(
        user.getId(),
        user.getEmail(),
        user.getName());
  }
}