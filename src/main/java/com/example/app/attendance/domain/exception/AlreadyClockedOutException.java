package com.example.app.attendance.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class AlreadyClockedOutException extends DomainException {
  public AlreadyClockedOutException(Long id) {
    super("ALREADY_CLOCKED_OUT", HttpStatus.CONFLICT, "Already Clocked Out : " + id);
  }
}
