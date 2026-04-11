package com.example.app.cultivationCycle.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class CultivationCycleNotFoundException extends DomainException {
  public CultivationCycleNotFoundException(Long id) {
    super(
        "CULTIVATION_CYCLE_NOT_FOUND", HttpStatus.NOT_FOUND, "CULTIVATION_CYCLE_NOT_FOUND : " + id);
  }
}
