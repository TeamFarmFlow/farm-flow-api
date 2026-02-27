package com.example.app.core.jwt;

import java.time.Instant;
import java.util.Date;

public record IssueAccessTokenResult(String accessToken, Instant expiresAt, long expiresIn) {
  public static IssueAccessTokenResult from(String accessToken, Date expiration) {
    return new IssueAccessTokenResult(
        accessToken,
        expiration.toInstant(),
        expiration.getTime() - System.currentTimeMillis());
  }
}
