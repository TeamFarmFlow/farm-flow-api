package com.example.app.auth.infrastructure.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(RefreshCookieProperties.class)
@RequiredArgsConstructor
public class RefreshTokenCookieParser {
  private final RefreshCookieProperties properties;

  public String parse(HttpServletRequest request) {
    String refreshToken = null;

    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (properties.name().equals(cookie.getName())) {
          refreshToken = cookie.getValue();
          break;
        }
      }
    }

    return refreshToken;
  }
}
