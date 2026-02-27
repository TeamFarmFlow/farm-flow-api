package com.example.app.core.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends DomainException {
  public ForbiddenException() {
    super("FORBIDDEN", HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
  }
}
