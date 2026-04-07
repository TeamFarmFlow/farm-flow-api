package com.example.app.attendance.domain;


import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceAdjustmentRepository extends JpaRepository<AttendanceAdjustment, Long> {
}
