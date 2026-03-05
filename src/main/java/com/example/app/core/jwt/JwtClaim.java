package com.example.app.core.jwt;

import com.example.app.user.domain.User;
import com.example.app.user.domain.enums.UserType;
import io.jsonwebtoken.Claims;
import lombok.Getter;

@Getter
public class JwtClaim {
  private final Long id;
  private final String email;

  public JwtClaim(Long id, String email) {
    this.id = id;
    this.email = email;
  }

  public static JwtClaim userOf(User user) {
    return new JwtClaim(user.getId(), user.getEmail());
  }

  public static JwtClaim claimsOf(Claims claims) {
    Long id = claims.get("id", Long.class);
    String email = claims.get("email", String.class);

    return new JwtClaim(id, email);
  }
}
