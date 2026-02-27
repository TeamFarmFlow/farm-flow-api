package com.example.app.user.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DomainException {
  public UserNotFoundException(Long id) {
    super("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "User not found with id: " + id);
  }

  public UserNotFoundException(String email) {
    super("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "User not found with email: " + email);
  }
}
