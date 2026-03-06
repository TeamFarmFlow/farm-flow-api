package com.example.app.invitation.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class AlreadyFarmInvitedException extends DomainException {

  public AlreadyFarmInvitedException(String email) {
    super(
        "ALREADY_FARM_INVITED",
        HttpStatus.CONFLICT,
        "Email is already invited to this farm: " + email);
  }
}
