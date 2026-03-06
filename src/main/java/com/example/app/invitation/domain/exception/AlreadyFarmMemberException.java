package com.example.app.invitation.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class AlreadyFarmMemberException extends DomainException {
  public AlreadyFarmMemberException(String email) {
    super("ALREADY_FARM_MEMBER", HttpStatus.CONFLICT, "Email is already a farm member: " + email);
  }
}
