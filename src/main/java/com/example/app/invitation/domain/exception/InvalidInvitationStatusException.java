package com.example.app.invitation.domain.exception;

import com.example.app.core.exception.DomainException;
import com.example.app.invitation.domain.enums.FarmInvitationStatus;
import org.springframework.http.HttpStatus;

public class InvalidInvitationStatusException extends DomainException {
  public InvalidInvitationStatusException(FarmInvitationStatus status) {
    super("INVALID_INVITATION_STAUS", HttpStatus.CONFLICT, getMessage(status));
  }

  private static String getMessage(FarmInvitationStatus status) {
    return switch (status) {
      case ACCEPTED -> "Invitation already accepted.";
      case EXPIRED -> "Invitation expired.";
      case CANCELED -> "Invitation canceled.";
      default -> "Invalid invitation status.";
    };
  }
}
