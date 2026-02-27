package com.example.app.core.exception;

import java.time.Instant;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public class ErrorResponse {
  private final String errorCode;
  private final String message;
  private final Integer statusCode;
  private final Instant timestamp;

  public ErrorResponse(String errorCode, String message, HttpStatus statusCode, Instant timestamp) {
    this.errorCode = errorCode;
    this.message = message;
    this.statusCode = statusCode.value();
    this.timestamp = timestamp;
  }

  public static ErrorResponse of(String errorCode, HttpStatus statusCode, String message) {
    return new ErrorResponse(errorCode, message, statusCode, Instant.now());
  }

  public static ErrorResponse domainExceptionOf(DomainException e) {
    return new ErrorResponse(e.getErrorCode(), e.getMessage(), e.getStatusCode(), Instant.now());
  }

  public static ErrorResponse methodArgumentNotValidExceptionOf(MethodArgumentNotValidException e) {
    String message =
        e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getDefaultMessage())
            .findFirst()
            .orElse("Validation failed");

    return new ErrorResponse("VALIDATION_ERROR", message, HttpStatus.BAD_REQUEST, Instant.now());
  }
}
