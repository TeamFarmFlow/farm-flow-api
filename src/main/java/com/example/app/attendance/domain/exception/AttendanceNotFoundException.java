package com.example.app.attendance.domain.exception;

import com.example.app.core.exception.DomainException;
import org.springframework.http.HttpStatus;

public class AttendanceNotFoundException extends DomainException {
    public AttendanceNotFoundException() {
        super("ATTENDANCE_NOT_FOUND", HttpStatus.NOT_FOUND, "Attendance not found");
    }
}
