package com.example.app.role.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends DomainException {
  public RoleNotFoundException() {
    super("ROLE_NOT_FOUND", HttpStatus.NOT_FOUND, "Role not found");
  }
}
