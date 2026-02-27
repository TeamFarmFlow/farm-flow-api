package com.example.app.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorResponse> handleDomainException(DomainException e) {
    return ResponseEntity
        .status(e.getStatusCode())
        .body(ErrorResponse.domainExceptionOf(e));
  }

  @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleNoHandlerFoundException() {
    return ErrorResponse.of("NOT_FOUND", HttpStatus.NOT_FOUND, "Resource not found");
  }

  @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleNoResourceFoundException() {
    return ErrorResponse.of("NOT_FOUND", HttpStatus.NOT_FOUND, "Resource not found");
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleUnexpected(Exception e) {
    return ErrorResponse.of("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
  }
}
