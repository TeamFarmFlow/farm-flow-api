package com.example.app.auth.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class WrongEmailOrPasswordException extends DomainException {
  public WrongEmailOrPasswordException() {
    super("WRONG_EMAIL_OR_PASSWORD", HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 잘못되었습니다");
  }
}
