package com.example.app.auth.application.result;

import com.example.app.user.domain.User;

public record AuthUser(Long id, String email, String name) {
  public static AuthUser from(User user) {
    return new AuthUser(user.getId(), user.getEmail(), user.getName());
  }
}
