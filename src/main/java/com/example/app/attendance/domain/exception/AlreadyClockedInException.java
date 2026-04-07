package com.example.app.attendance.domain.exception;


import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class AlreadyClockedInException extends DomainException {
    public AlreadyClockedInException() {
        super("ALREADY_CLOCK_IN", HttpStatus.CONFLICT, "already clocked In");
    }
}
