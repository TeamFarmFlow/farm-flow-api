package com.example.app.attendance.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
  boolean existsByFarm_IdAndUser_IdAndWorkDate(Long farmId, Long userId, LocalDate workDate);

  Optional<Attendance> findByFarm_IdAndUser_IdAndWorkDate(
      Long farmId, Long userId, LocalDate workDate);

  Optional<Attendance> findByIdAndFarm_Id(Long id, Long farmId);

  List<Attendance> findAllByFarm_IdAndUser_IdAndWorkDateBetweenOrderByWorkDateDesc(
      Long farmId, Long userId, LocalDate from, LocalDate to);

  @Query(
      """
      select a
      from Attendance a
      join fetch a.user u
      where a.farm.id = :farmId
        and a.workDate between :from and :to
      order by a.workDate desc
      """)
  List<Attendance> findAllByFarmIdAndWorkDateBetweenOrderByWorkDateDescWithUser(
      @Param("farmId") Long farmId, @Param("from") LocalDate from, @Param("to") LocalDate to);
}
