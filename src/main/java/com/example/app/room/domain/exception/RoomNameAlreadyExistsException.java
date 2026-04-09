package com.example.app.room.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class RoomNameAlreadyExistsException extends DomainException {
    public RoomNameAlreadyExistsException(String name) {
        super(
                "ROOM_NAME_ALREADY_EXISTS",
                HttpStatus.CONFLICT,
                "Room name already exists: " + name);
    }
}
