package com.example.app.auth.application.result;

import com.example.app.user.domain.User;
import com.example.app.user.domain.enums.UserStatus;
import com.example.app.user.domain.enums.UserType;
import java.time.Instant;

public record AuthUser(
    Long id,
    UserType type,
    String email,
    String name,
    UserStatus status,
    Instant createdAt,
    Instant updatedAt) {
  public static AuthUser from(User user) {
    return new AuthUser(
        user.getId(),
        user.getType(),
        user.getEmail(),
        user.getName(),
        user.getStatus(),
        user.getCreatedAt(),
        user.getUpdatedAt());
  }
}
