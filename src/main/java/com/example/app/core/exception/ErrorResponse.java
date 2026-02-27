package com.example.app.core.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ErrorResponse {
  private final String errorCode;
  private final String message;
  private final HttpStatus statusCode;
  private final Instant timestamp;

  public ErrorResponse(String errorCode, String message, HttpStatus statusCode, Instant timestamp) {
    this.errorCode = errorCode;
    this.message = message;
    this.statusCode = statusCode;
    this.timestamp = timestamp;
  }

  public static ErrorResponse of(String errorCode, HttpStatus statusCode, String message) {
    return new ErrorResponse(errorCode, message, statusCode, Instant.now());
  }

  public static ErrorResponse domainExceptionOf(DomainException e) {
    return new ErrorResponse(e.getErrorCode(), e.getMessage(), e.getStatusCode(), Instant.now());
  }
}