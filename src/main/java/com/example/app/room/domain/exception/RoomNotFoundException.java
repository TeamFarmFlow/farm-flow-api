package com.example.app.room.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class RoomNotFoundException extends DomainException {
  public RoomNotFoundException(Long id) {
    super("ROOM_NOT_FOUND", HttpStatus.NOT_FOUND, "room not found : " + id);
  }
}
