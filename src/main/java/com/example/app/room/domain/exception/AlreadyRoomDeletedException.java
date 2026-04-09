package com.example.app.room.domain.exception;


import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class AlreadyRoomDeletedException extends DomainException {
    public AlreadyRoomDeletedException(Long id) {
        super("ALREADY_ROOM_DELETED", HttpStatus.CONFLICT, "Already room deleted : " + id);
    }
}
