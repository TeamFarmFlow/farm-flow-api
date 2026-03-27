package com.example.app.farmUser.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class FarmUserNotFoundException extends DomainException {
  public FarmUserNotFoundException(Long userId) {
    super("FARMUSER_NOT_FOUND", HttpStatus.NOT_FOUND, "farmUser not found : " + userId);
  }
}
