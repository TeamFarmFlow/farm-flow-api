package com.example.app.farm.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class FarmNotFoundException extends DomainException {
  public FarmNotFoundException(Long id) {
    super("FARM_NOT_FOUND", HttpStatus.NOT_FOUND, "Farm not found with id: " + id);
  }
}
