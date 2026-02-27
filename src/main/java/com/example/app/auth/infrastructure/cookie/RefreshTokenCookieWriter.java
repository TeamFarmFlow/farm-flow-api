package com.example.app.auth.infrastructure.cookie;

import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(RefreshCookieProperties.class)
@RequiredArgsConstructor
public class RefreshTokenCookieWriter {
  private final RefreshCookieProperties properties;

  private ResponseCookie.ResponseCookieBuilder createBuilder(String refreshToken) {
    return ResponseCookie.from(properties.name(), refreshToken)
        .httpOnly(true)
        .secure(properties.secure())
        .path(properties.path())
        .sameSite(properties.sameSite())
        .maxAge(Duration.ofDays(20).toSeconds());
  }

  public void set(HttpServletResponse response, String refreshToken) {
    response.addHeader(HttpHeaders.SET_COOKIE, createBuilder(refreshToken).build().toString());
  }

  public void clear(HttpServletResponse response) {
    response.addHeader(HttpHeaders.SET_COOKIE, createBuilder(null).build().toString());
  }
}
