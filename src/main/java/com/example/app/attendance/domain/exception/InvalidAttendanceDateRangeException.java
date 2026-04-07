package com.example.app.attendance.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidAttendanceDateRangeException extends DomainException {
    public InvalidAttendanceDateRangeException() {
        super("INVALID_ATTENDANCE_DATE_RANGE",
                HttpStatus.BAD_REQUEST,
                "from must be less than or equal to to");
    }
}