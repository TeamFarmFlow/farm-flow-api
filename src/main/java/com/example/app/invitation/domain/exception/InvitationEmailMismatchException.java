package com.example.app.invitation.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvitationEmailMismatchException extends DomainException {
  public InvitationEmailMismatchException() {
    super(
        "EMAIL_MISMATCHED",
        HttpStatus.CONFLICT,
        "This invitation is not assigned to your account.");
  }
}
