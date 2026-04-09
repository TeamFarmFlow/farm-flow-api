package com.example.app.room.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidRoomStatusException extends DomainException {
  public InvalidRoomStatusException() {
    super(
        "INVALID_ROOM_STATUS",
        HttpStatus.BAD_REQUEST,
        "Room status can only be ACTIVE or INACTIVE");
  }
}
