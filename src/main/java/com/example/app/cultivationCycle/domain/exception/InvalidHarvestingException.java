package com.example.app.cultivationCycle.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidHarvestingException extends DomainException {
  public InvalidHarvestingException() {
    super("INVALID_HARVESTING", HttpStatus.BAD_REQUEST, "INVALID_HARVESTING");
  }
}
