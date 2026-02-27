package com.example.app.auth.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends DomainException {
  public InvalidTokenException() {
    super("INVALID_TOKEN", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다");
  }
}
