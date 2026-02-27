package com.example.app.auth.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends DomainException {
  public DuplicateEmailException(String email) {
    super("DUPLICATE_EMAIL", HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다: " + email);
  }
}
