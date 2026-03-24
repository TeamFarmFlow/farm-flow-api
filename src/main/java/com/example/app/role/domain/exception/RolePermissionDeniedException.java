package com.example.app.role.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class RolePermissionDeniedException extends DomainException {
  public RolePermissionDeniedException() {
    super("ROLE_PERMISSION_DENIED", HttpStatus.NOT_FOUND, "Permission denied to manage roles.");
  }
}
