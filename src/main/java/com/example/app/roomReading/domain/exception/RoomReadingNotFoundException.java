package com.example.app.roomReading.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class RoomReadingNotFoundException extends DomainException {
  public RoomReadingNotFoundException(Long id) {
    super(
        "ROOM_READING_NOT_FOUND",
        HttpStatus.NOT_FOUND,
        "Room reading with id " + id + " not found");
  }
}
