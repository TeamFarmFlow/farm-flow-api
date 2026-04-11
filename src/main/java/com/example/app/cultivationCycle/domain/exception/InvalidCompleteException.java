package com.example.app.cultivationCycle.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidCompleteException extends DomainException {
  public InvalidCompleteException() {
    super("INVALID_CYCLE_COMPLETE", HttpStatus.BAD_REQUEST, "INVALID_CYCLE_COMPLETE");
  }
}
