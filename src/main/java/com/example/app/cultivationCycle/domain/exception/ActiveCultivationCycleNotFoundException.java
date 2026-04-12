package com.example.app.cultivationCycle.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class ActiveCultivationCycleNotFoundException extends DomainException {
  public ActiveCultivationCycleNotFoundException(Long roomId) {
    super(
        "ACTIVE_CULTIVATION_CYCLE_NOT_FOUND",
        HttpStatus.NOT_FOUND,
        "active cultivation cycle not found (roomId) : " + roomId);
  }
}
