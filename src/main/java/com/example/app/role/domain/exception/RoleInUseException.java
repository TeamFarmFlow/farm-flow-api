package com.example.app.role.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class RoleInUseException extends DomainException {
  public RoleInUseException() {
    super(
        "ROLE_IN_USE",
        HttpStatus.CONFLICT,
        "This role is currently assigned to users and cannot be deleted.");
  }
}
