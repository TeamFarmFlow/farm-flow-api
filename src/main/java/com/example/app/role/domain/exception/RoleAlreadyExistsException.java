package com.example.app.role.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class RoleAlreadyExistsException extends DomainException {
  public RoleAlreadyExistsException(String roleKey) {
    super("ROLE_ALREADY_EXISTS", HttpStatus.CONFLICT, "Role already exists: " + roleKey);
  }
}
