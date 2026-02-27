package com.example.app.core.jwt;

import com.example.app.user.domain.User;
import com.example.app.user.domain.enums.UserType;

import io.jsonwebtoken.Claims;
import lombok.Getter;

@Getter
public class JwtClaim {
  private final Long id;
  private final String email;
  private final UserType type;

  public JwtClaim(Long id, String email, UserType type) {
    this.id = id;
    this.email = email;
    this.type = type;
  }

  public static JwtClaim userOf(User user) {
    return new JwtClaim(user.getId(), user.getEmail(), user.getType());
  }

  public static JwtClaim claimsOf(Claims claims) {
    Long id = claims.get("id", Long.class);
    String email = claims.get("email", String.class);
    UserType type = UserType.valueOf(claims.get("type", String.class));

    return new JwtClaim(id, email, type);
  }
}