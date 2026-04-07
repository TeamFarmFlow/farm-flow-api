package com.example.app.attendance.application;

import com.example.app.attendance.domain.Attendance;
import com.example.app.attendance.domain.AttendanceRepository;
import com.example.app.attendance.domain.exception.AlreadyClockedInException;
import com.example.app.attendance.domain.exception.AlreadyClockedOutException;
import com.example.app.attendance.domain.exception.AttendanceNotFoundException;
import com.example.app.attendance.presetation.dto.response.AttendanceResponse;
import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.farmUser.domain.FarmUserRepository;
import com.example.app.farmUser.domain.enums.FarmUserStatus;
import com.example.app.farmUser.domain.exception.FarmUserNotFoundException;
import com.example.app.user.domain.User;
import com.example.app.user.domain.UserRepository;
import com.example.app.user.domain.exception.UserNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {
  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

  private final AttendanceRepository attendanceRepository;
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

    User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    Attendance attendance = Attendance.clockIn(farm, user, workDate, clockInAt);
    attendanceRepository.save(attendance);

    return new AttendanceResponse(
        attendance.getId(),
        userId,
        user.getName(),
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

    User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    return new AttendanceResponse(
        attendance.getId(),
        userId,
        user.getName(),
        attendance.getWorkDate(),
        attendance.getClockInAt(),
        attendance.getClockOutAt(),
        attendance.getStatus());
  }
}
