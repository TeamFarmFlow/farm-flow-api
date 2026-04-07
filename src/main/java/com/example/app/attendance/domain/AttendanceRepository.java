package com.example.app.attendance.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
  boolean existsByFarm_IdAndUser_IdAndWorkDate(Long farmId, Long userId, LocalDate workDate);

  Optional<Attendance> findByFarm_IdAndUser_IdAndWorkDate(
      Long farmId, Long userId, LocalDate workDate);

  List<Attendance> findAllByFarm_IdAndUser_IdAndWorkDateBetweenOrderByWorkDateDesc(
      Long farmId, Long userId, LocalDate from, LocalDate to);
}
