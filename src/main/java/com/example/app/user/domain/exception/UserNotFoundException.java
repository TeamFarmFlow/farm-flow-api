package com.example.app.user.domain.exception;

import org.springframework.http.HttpStatus;

import com.example.app.core.exception.DomainException;

public class UserNotFoundException extends DomainException {
  public UserNotFoundException(Long id) {
    super("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "User not found with id: " + id);
  }
}
