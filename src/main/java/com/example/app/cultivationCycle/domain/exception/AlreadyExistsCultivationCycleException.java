package com.example.app.cultivationCycle.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class AlreadyExistsCultivationCycleException extends DomainException {
  public AlreadyExistsCultivationCycleException(Long roomId) {
    super(
        "ALREADY_EXISTS_CULTIVATION_CYCLE",
        HttpStatus.CONFLICT,
        "active cultivation cycle already exists for room : " + roomId);
  }
}
