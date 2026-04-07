package com.example.app.attendance.domain;

import com.example.app.shared.entity.BaseTimeEntity;
import com.example.app.user.domain.User;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    name = "attendance_adjustments",
    indexes = {
      @Index(name = "idx_attendance_adjustment_attendance", columnList = "attendance_id"),
      @Index(name = "idx_attendance_adjustment_adjusted_by", columnList = "adjusted_by")
    })
@Builder
public class AttendanceAdjustment extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "attendance_id", nullable = false)
  private Attendance attendance;

  @Column(name = "before_clock_in_at")
  private Instant beforeClockInAt;

  @Column(name = "before_clock_out_at")
  private Instant beforeClockOutAt;

  @Column(name = "after_clock_in_at")
  private Instant afterClockInAt;

  @Column(name = "after_clock_out_at")
  private Instant afterClockOutAt;

  @Column(name = "reason", nullable = false, length = 255)
  private String reason;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "adjusted_by", nullable = false)
  private User adjustedBy;

  @Column(name = "adjusted_at", nullable = false)
  private Instant adjustedAt;

  public static AttendanceAdjustment create(
      Attendance attendance,
      Instant beforeClockInAt,
      Instant beforeClockOutAt,
      Instant afterClockInAt,
      Instant afterClockOutAt,
      String reason,
      User adjustedBy,
      Instant adjustedAt) {
    return AttendanceAdjustment.builder()
        .attendance(attendance)
        .beforeClockInAt(beforeClockInAt)
        .beforeClockOutAt(beforeClockOutAt)
        .afterClockInAt(afterClockInAt)
        .afterClockOutAt(afterClockOutAt)
        .reason(reason)
        .adjustedBy(adjustedBy)
        .adjustedAt(adjustedAt)
        .build();
  }
}
