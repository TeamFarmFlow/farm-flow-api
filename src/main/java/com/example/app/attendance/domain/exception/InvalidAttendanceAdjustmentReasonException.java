package com.example.app.attendance.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidAttendanceAdjustmentReasonException extends DomainException {
  public InvalidAttendanceAdjustmentReasonException() {
    super(
        "INVALID_ATTENDANCE_ADJUSTMENT_REASON", HttpStatus.BAD_REQUEST, "reason must not be blank");
  }
}
