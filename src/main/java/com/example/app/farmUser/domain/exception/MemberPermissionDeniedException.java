package com.example.app.farmUser.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class MemberPermissionDeniedException extends DomainException {
  public MemberPermissionDeniedException() {
    super("MEMBER_PERMISSION_DENIED", HttpStatus.FORBIDDEN, "Permission denied to manage members.");
  }
}
