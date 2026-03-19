package com.example.app.invitation.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvitationNotFoundException extends DomainException {
  public InvitationNotFoundException(String code) {
    super("INVITATION_NOT_FOUND", HttpStatus.NOT_FOUND, "Invitation not found with code: " + code);
  }
}
