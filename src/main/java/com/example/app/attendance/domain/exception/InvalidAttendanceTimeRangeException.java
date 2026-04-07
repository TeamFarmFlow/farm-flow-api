package com.example.app.attendance.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidAttendanceTimeRangeException extends DomainException {
  public InvalidAttendanceTimeRangeException() {
    super(
        "INVALID_ATTENDANCE_TIME_RANGE",
        HttpStatus.BAD_REQUEST,
        "clockInAt must not be null and clockOutAt must be greater than or equal to clockInAt");
  }
}
