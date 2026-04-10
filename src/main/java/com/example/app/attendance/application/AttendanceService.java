package com.example.app.attendance.application;

import com.example.app.attendance.application.command.AttendanceUpdateCommand;
import com.example.app.attendance.domain.Attendance;
import com.example.app.attendance.domain.AttendanceAdjustment;
import com.example.app.attendance.domain.AttendanceAdjustmentRepository;
import com.example.app.attendance.domain.AttendanceRepository;
import com.example.app.attendance.domain.exception.AlreadyClockedInException;
import com.example.app.attendance.domain.exception.AlreadyClockedOutException;
import com.example.app.attendance.domain.exception.AttendanceNotFoundException;
import com.example.app.attendance.domain.exception.InvalidAttendanceAdjustmentReasonException;
import com.example.app.attendance.domain.exception.InvalidAttendanceDateRangeException;
import com.example.app.attendance.domain.exception.InvalidAttendanceTimeRangeException;
import com.example.app.attendance.presentation.dto.response.AttendanceResponse;
import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.farmUser.domain.FarmUserRepository;
import com.example.app.farmUser.domain.enums.FarmUserStatus;
import com.example.app.farmUser.domain.exception.FarmUserNotFoundException;
import com.example.app.farmUser.domain.exception.MemberPermissionDeniedException;
import com.example.app.role.domain.enums.PermissionKey;
import com.example.app.user.domain.User;
import com.example.app.user.domain.UserRepository;
import com.example.app.user.domain.exception.UserNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {
  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Seoul");

  private final AttendanceRepository attendanceRepository;
  private final AttendanceAdjustmentRepository attendanceAdjustmentRepository;
  private final FarmRepository farmRepository;
  private final FarmUserRepository farmUserRepository;
  private final UserRepository userRepository;

  @Transactional
  public AttendanceResponse clockIn(Long farmId, Long userId) {
    Farm farm =
        farmRepository
            .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
            .orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean existsFarmUser =
        farmUserRepository.existsByFarm_IdAndUser_IdAndStatus(
            farmId, userId, FarmUserStatus.ACTIVE);
    if (!existsFarmUser) {
      throw new FarmUserNotFoundException(userId);
    }

    ZonedDateTime now = ZonedDateTime.now(DEFAULT_ZONE_ID);
    LocalDate workDate = now.toLocalDate();
    Instant clockInAt = now.toInstant();

    boolean alreadyClockedIn =
        attendanceRepository.existsByFarm_IdAndUser_IdAndWorkDate(farmId, userId, workDate);
    if (alreadyClockedIn) {
      throw new AlreadyClockedInException();
    }

    User user =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    Attendance attendance = Attendance.clockIn(farm, user, workDate, clockInAt);
    attendanceRepository.save(attendance);

    return new AttendanceResponse(
        attendance.getId(),
        attendance.getUser().getId(),
        attendance.getUser().getName(),
        attendance.getWorkDate(),
        attendance.getClockInAt(),
        attendance.getClockOutAt(),
        attendance.getStatus());
  }

  @Transactional
  public AttendanceResponse clockOut(Long farmId, Long userId) {
    farmRepository
        .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
        .orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean existsFarmUser =
        farmUserRepository.existsByFarm_IdAndUser_IdAndStatus(
            farmId, userId, FarmUserStatus.ACTIVE);
    if (!existsFarmUser) {
      throw new FarmUserNotFoundException(userId);
    }

    ZonedDateTime now = ZonedDateTime.now(DEFAULT_ZONE_ID);
    LocalDate workDate = now.toLocalDate();
    Instant clockOutAt = now.toInstant();

    Attendance attendance =
        attendanceRepository
            .findByFarm_IdAndUser_IdAndWorkDate(farmId, userId, workDate)
            .orElseThrow(AttendanceNotFoundException::new);
    if (!Objects.isNull(attendance.getClockOutAt())) {
      throw new AlreadyClockedOutException(attendance.getId());
    }

    attendance.clockOut(clockOutAt);

    return new AttendanceResponse(
        attendance.getId(),
        attendance.getUser().getId(),
        attendance.getUser().getName(),
        attendance.getWorkDate(),
        attendance.getClockInAt(),
        attendance.getClockOutAt(),
        attendance.getStatus());
  }

  public List<AttendanceResponse> getMyAttendances(
      Long farmId, Long userId, LocalDate from, LocalDate to) {
    if (from.isAfter(to)) {
      throw new InvalidAttendanceDateRangeException();
    }

    farmRepository
        .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
        .orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean existsFarmUser =
        farmUserRepository.existsByFarm_IdAndUser_IdAndStatus(
            farmId, userId, FarmUserStatus.ACTIVE);
    if (!existsFarmUser) {
      throw new FarmUserNotFoundException(userId);
    }

    List<Attendance> attendances =
        attendanceRepository.findAllByFarm_IdAndUser_IdAndWorkDateBetweenOrderByWorkDateDesc(
            farmId, userId, from, to);

    return attendances.stream()
        .map(
            attendance ->
                new AttendanceResponse(
                    attendance.getId(),
                    attendance.getUser().getId(),
                    attendance.getUser().getName(),
                    attendance.getWorkDate(),
                    attendance.getClockInAt(),
                    attendance.getClockOutAt(),
                    attendance.getStatus()))
        .toList();
  }

  public List<AttendanceResponse> getAttendances(
      Long farmId, Long userId, LocalDate from, LocalDate to) {
    if (from.isAfter(to)) {
      throw new InvalidAttendanceDateRangeException();
    }

    farmRepository
        .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
        .orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean isManageMember =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.ATTENDANCE_MANAGE);
    if (!isManageMember) {
      throw new MemberPermissionDeniedException();
    }

    List<Attendance> attendances =
        attendanceRepository.findAllByFarmIdAndWorkDateBetweenOrderByWorkDateDescWithUser(
            farmId, from, to);

    return attendances.stream()
        .map(
            attendance ->
                new AttendanceResponse(
                    attendance.getId(),
                    attendance.getUser().getId(),
                    attendance.getUser().getName(),
                    attendance.getWorkDate(),
                    attendance.getClockInAt(),
                    attendance.getClockOutAt(),
                    attendance.getStatus()))
        .toList();
  }

  @Transactional
  public AttendanceResponse updateAttendance(
      Long id, Long farmId, Long userId, AttendanceUpdateCommand command) {
    farmRepository
        .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
        .orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean isManageMember =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.ATTENDANCE_MANAGE);
    if (!isManageMember) {
      throw new MemberPermissionDeniedException();
    }

    validateAttendanceUpdate(command);

    Attendance attendance =
        attendanceRepository
            .findByIdAndFarm_Id(id, farmId)
            .orElseThrow(AttendanceNotFoundException::new);

    User user =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    Instant adjustedAt = ZonedDateTime.now(DEFAULT_ZONE_ID).toInstant();
    Instant adjustedClockInAt = toInstant(command.clockInAt());
    Instant adjustedClockOutAt =
        command.clockOutAt() == null ? null : toInstant(command.clockOutAt());

    AttendanceAdjustment attendanceAdjustment =
        AttendanceAdjustment.create(
            attendance,
            attendance.getClockInAt(),
            attendance.getClockOutAt(),
            adjustedClockInAt,
            adjustedClockOutAt,
            command.reason(),
            user,
            adjustedAt);
    attendanceAdjustmentRepository.save(attendanceAdjustment);

    attendance.adjust(adjustedClockInAt, adjustedClockOutAt, command.reason());

    return new AttendanceResponse(
        attendance.getId(),
        attendance.getUser().getId(),
        attendance.getUser().getName(),
        attendance.getWorkDate(),
        attendance.getClockInAt(),
        attendance.getClockOutAt(),
        attendance.getStatus());
  }

  private void validateAttendanceUpdate(AttendanceUpdateCommand command) {
    if (command.clockInAt() == null
        || (command.clockOutAt() != null && command.clockOutAt().isBefore(command.clockInAt()))) {
      throw new InvalidAttendanceTimeRangeException();
    }

    if (!StringUtils.hasText(command.reason())) {
      throw new InvalidAttendanceAdjustmentReasonException();
    }
  }

  private Instant toInstant(LocalDateTime localDateTime) {
    return localDateTime.atZone(DEFAULT_ZONE_ID).toInstant();
  }
}
