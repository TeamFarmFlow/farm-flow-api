package com.example.app.core.exception;

import org.springframework.http.HttpStatus;

public abstract class DomainException extends RuntimeException {

  private final String errorCode;
  private final HttpStatus statusCode;
  private final String message;

  protected DomainException(String errorCode, HttpStatus statusCode, String message) {
    super();

    this.errorCode = errorCode;
    this.statusCode = statusCode;
    this.message = message;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public HttpStatus getStatusCode() {
    return statusCode;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
