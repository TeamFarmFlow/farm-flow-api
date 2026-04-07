package com.example.app.attendance.domain;

import com.example.app.attendance.domain.enums.AttendanceStatus;
import com.example.app.farm.domain.Farm;
import com.example.app.shared.entity.BaseTimeEntity;
import com.example.app.user.domain.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    name = "attendances",
    indexes = {
      @Index(name = "idx_attendance_farm_work_date", columnList = "farm_id,work_date"),
      @Index(name = "idx_attendance_user_work_date", columnList = "user_id,work_date")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_attendance_farm_user_work_date",
          columnNames = {"farm_id", "user_id", "work_date"})
    })
@Builder
public class Attendance extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "farm_id", nullable = false)
  private Farm farm;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "work_date", nullable = false)
  private LocalDate workDate;

  @Column(name = "clock_in_at", nullable = false)
  private Instant clockInAt;

  @Column(name = "clock_out_at")
  private Instant clockOutAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private AttendanceStatus status;

  @Column(name = "note", length = 255)
  private String note;

  public static Attendance clockIn(Farm farm, User user, LocalDate workDate, Instant clockInAt) {
    return Attendance.builder()
        .farm(farm)
        .user(user)
        .workDate(workDate)
        .clockInAt(clockInAt)
        .status(AttendanceStatus.WORKING)
        .build();
  }

  public void clockOut(Instant clockOutAt) {
    this.clockOutAt = clockOutAt;
    this.status = AttendanceStatus.COMPLETED;
  }

  public void adjust(Instant clockInAt, Instant clockOutAt, String note) {
    this.clockInAt = clockInAt;
    this.clockOutAt = clockOutAt;
    this.note = note;
    this.status = clockOutAt == null ? AttendanceStatus.WORKING : AttendanceStatus.COMPLETED;
  }

  public boolean isCompleted() {
    return this.status == AttendanceStatus.COMPLETED;
  }
}
