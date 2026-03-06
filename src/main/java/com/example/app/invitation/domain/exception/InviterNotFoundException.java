package com.example.app.invitation.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InviterNotFoundException extends DomainException {
  public InviterNotFoundException(Long ownerId) {
    super("Inviter_NOT_FOUND", HttpStatus.NOT_FOUND, "Inviter not found: " + ownerId);
  }
}
