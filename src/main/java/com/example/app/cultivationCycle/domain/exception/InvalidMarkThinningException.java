package com.example.app.cultivationCycle.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidMarkThinningException extends DomainException {
  public InvalidMarkThinningException() {
    super("INVALID_MARK_THINNING", HttpStatus.BAD_REQUEST, "INVALID_MARK_THINNING");
  }
}
