package com.example.app.cultivationCycle.domain.exception;

import com.example.app.core.exception.DomainException;
import com.example.app.room.domain.enums.RoomStatus;
import org.springframework.http.HttpStatus;

public class InvalidCultivationCycleRegisterStatusException extends DomainException {
  public InvalidCultivationCycleRegisterStatusException(RoomStatus status) {
    super(
        "INVALID_CULTIVATION_CYCLE_REGISTER_STATUS",
        HttpStatus.BAD_REQUEST,
        "cultivation cycle can only be registered when room status is ACTIVE : " + status);
  }
}
