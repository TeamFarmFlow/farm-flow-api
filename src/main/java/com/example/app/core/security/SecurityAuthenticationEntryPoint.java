package com.example.app.core.security;

import com.example.app.core.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {

    String errorCode = "UNAUTHORIZED";
    String message = authException.getMessage();

    if ("Expired token".equals(message)) {
      errorCode = "EXPIRED_TOKEN";
    } else if ("Invalid token".equals(message)) {
      errorCode = "INVALID_TOKEN";
    } else {
      message = "Unauthorized";
    }

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json;charset=UTF-8");

    ErrorResponse errorResponse = ErrorResponse.of(errorCode, HttpStatus.UNAUTHORIZED, message);

    objectMapper.writeValue(response.getWriter(), errorResponse);
  }
}
