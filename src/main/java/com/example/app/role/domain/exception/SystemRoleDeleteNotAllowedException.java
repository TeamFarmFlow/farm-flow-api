package com.example.app.role.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class SystemRoleDeleteNotAllowedException extends DomainException {
  public SystemRoleDeleteNotAllowedException() {
    super(
        "SYSTEM_ROLE_DELETE_NOT_ALLOWED", HttpStatus.FORBIDDEN, "System roles cannot be deleted.");
  }
}
