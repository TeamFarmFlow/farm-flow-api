package com.example.app.cultivationCycle.application;

import com.example.app.cultivationCycle.application.command.CultivationCycleCompleteCommand;
import com.example.app.cultivationCycle.application.command.CultivationCycleHarvestingCommand;
import com.example.app.cultivationCycle.application.command.CultivationCycleMarkThinningCommand;
import com.example.app.cultivationCycle.domain.CultivationCycle;
import com.example.app.cultivationCycle.domain.CultivationCycleRepository;
import com.example.app.cultivationCycle.domain.enums.CultivationCycleStatus;
import com.example.app.cultivationCycle.domain.exception.ActiveCultivationCycleNotFoundException;
import com.example.app.cultivationCycle.domain.exception.AlreadyExistsCultivationCycleException;
import com.example.app.cultivationCycle.domain.exception.CultivationCycleNotFoundException;
import com.example.app.cultivationCycle.domain.exception.InvalidCompleteException;
import com.example.app.cultivationCycle.domain.exception.InvalidCultivationCycleRegisterStatusException;
import com.example.app.cultivationCycle.domain.exception.InvalidHarvestingException;
import com.example.app.cultivationCycle.domain.exception.InvalidMarkThinningException;
import com.example.app.cultivationCycle.presentation.dto.response.CultivationCycleResponse;
import com.example.app.farm.application.FarmAccessValidator;
import com.example.app.farmUser.domain.FarmUserRepository;
import com.example.app.farmUser.domain.enums.FarmUserStatus;
import com.example.app.farmUser.domain.exception.MemberPermissionDeniedException;
import com.example.app.role.domain.enums.PermissionKey;
import com.example.app.room.domain.Room;
import com.example.app.room.domain.RoomRepository;
import com.example.app.room.domain.enums.RoomStatus;
import com.example.app.room.domain.exception.RoomNotFoundException;
import com.example.app.roomReading.domain.RoomReading;
import com.example.app.roomReading.presentation.dto.response.RoomReadingResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CultivationCycleService {
  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Seoul");
  private static final List<CultivationCycleStatus> ACTIVE_CYCLE_STATUSES =
      List.of(
          CultivationCycleStatus.IN_PROGRESS,
          CultivationCycleStatus.THINNED,
          CultivationCycleStatus.HARVESTING);

  private final CultivationCycleRepository cultivationCycleRepository;
  private final FarmAccessValidator farmAccessValidator;
  private final RoomRepository roomRepository;
  private final FarmUserRepository farmUserRepository;

  @Transactional
  public CultivationCycleResponse register(Long farmId, Long roomId, Long userId) {
    farmAccessValidator.validateActiveFarm(farmId);

    Room room = getRoomOrThrow(farmId, roomId);
    if (room.getStatus() != RoomStatus.ACTIVE) {
      throw new InvalidCultivationCycleRegisterStatusException(room.getStatus());
    }

    boolean isManageCultivationCycle =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.CYCLE_START);
    if (!isManageCultivationCycle) {
      throw new MemberPermissionDeniedException();
    }

    boolean existsCycle =
        cultivationCycleRepository.existsByRoomIdAndStatusIn(roomId, ACTIVE_CYCLE_STATUSES);
    if (existsCycle) {
      throw new AlreadyExistsCultivationCycleException(roomId);
    }

    ZonedDateTime now = ZonedDateTime.now(DEFAULT_ZONE_ID);
    LocalDate inDate = now.toLocalDate();

    CultivationCycle cultivationCycle = CultivationCycle.create(room, inDate);
    cultivationCycleRepository.save(cultivationCycle);

    return toResponse(cultivationCycle, room, false);
  }

  public CultivationCycleResponse getCycle(Long id, Long farmId, Long roomId, Long userId) {
    farmAccessValidator.validateActiveFarm(farmId);
    farmAccessValidator.validateActiveMember(farmId, userId);

    Room room = getRoomOrThrow(farmId, roomId);
    CultivationCycle cultivationCycle =
        cultivationCycleRepository
            .findByIdAndRoom_IdAndRoom_Farm_Id(id, roomId, farmId)
            .orElseThrow(() -> new CultivationCycleNotFoundException(id));

    return toResponse(cultivationCycle, room, true);
  }

  public List<CultivationCycleResponse> getCultivationCycles(
      Long farmId, Long roomId, Long userId) {
    farmAccessValidator.validateActiveFarm(farmId);
    farmAccessValidator.validateActiveMember(farmId, userId);

    Room room = getRoomOrThrow(farmId, roomId);

    List<CultivationCycle> cultivationCycles =
        cultivationCycleRepository.findAllByRoom_IdOrderByInDateDescIdDesc(roomId);

    return cultivationCycles.stream()
        .map(cultivationCycle -> toResponse(cultivationCycle, room, false))
        .toList();
  }

  @Transactional
  public CultivationCycleResponse markThinning(
      CultivationCycleMarkThinningCommand command, Long farmId, Long roomId, Long id, Long userId) {
    farmAccessValidator.validateActiveFarm(farmId);

    Room room = getRoomOrThrow(farmId, roomId);
    if (room.getStatus() != RoomStatus.ACTIVE) {
      throw new InvalidCultivationCycleRegisterStatusException(room.getStatus());
    }

    boolean isManageCultivationCycle =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.CYCLE_THINNING);
    if (!isManageCultivationCycle) {
      throw new MemberPermissionDeniedException();
    }

    CultivationCycle cultivationCycle =
        cultivationCycleRepository
            .findByIdAndRoom_IdAndRoom_Farm_Id(id, roomId, farmId)
            .orElseThrow(() -> new CultivationCycleNotFoundException(id));

    if (cultivationCycle.getStatus() != CultivationCycleStatus.IN_PROGRESS) {
      throw new InvalidMarkThinningException();
    }

    ZonedDateTime now = ZonedDateTime.now(DEFAULT_ZONE_ID);
    LocalDate date = now.toLocalDate();
    cultivationCycle.setThinningDate(command.note(), date);

    return toResponse(cultivationCycle, room, false);
  }

  @Transactional
  public CultivationCycleResponse markHarvestStart(
      Long farmId, Long roomId, Long userId, Long id, CultivationCycleHarvestingCommand command) {
    farmAccessValidator.validateActiveFarm(farmId);

    Room room = getRoomOrThrow(farmId, roomId);
    if (room.getStatus() != RoomStatus.ACTIVE) {
      throw new InvalidCultivationCycleRegisterStatusException(room.getStatus());
    }

    boolean isManageCultivationCycle =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.CYCLE_HARVEST);
    if (!isManageCultivationCycle) {
      throw new MemberPermissionDeniedException();
    }

    CultivationCycle cultivationCycle =
        cultivationCycleRepository
            .findByIdAndRoom_IdAndRoom_Farm_Id(id, roomId, farmId)
            .orElseThrow(() -> new CultivationCycleNotFoundException(id));

    if (cultivationCycle.getStatus() != CultivationCycleStatus.THINNED) {
      throw new InvalidHarvestingException();
    }

    ZonedDateTime now = ZonedDateTime.now(DEFAULT_ZONE_ID);
    LocalDate date = now.toLocalDate();
    cultivationCycle.setHarvestStartDate(command.note(), date);

    return toResponse(cultivationCycle, room, false);
  }

  @Transactional
  public CultivationCycleResponse complete(
      Long farmId, Long roomId, Long userId, Long id, CultivationCycleCompleteCommand command) {
    farmAccessValidator.validateActiveFarm(farmId);

    Room room = getRoomOrThrow(farmId, roomId);
    if (room.getStatus() != RoomStatus.ACTIVE) {
      throw new InvalidCultivationCycleRegisterStatusException(room.getStatus());
    }

    boolean isManageCultivationCycle =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.CYCLE_END);
    if (!isManageCultivationCycle) {
      throw new MemberPermissionDeniedException();
    }

    CultivationCycle cultivationCycle =
        cultivationCycleRepository
            .findByIdAndRoom_IdAndRoom_Farm_Id(id, roomId, farmId)
            .orElseThrow(() -> new CultivationCycleNotFoundException(id));

    if (cultivationCycle.getStatus() != CultivationCycleStatus.HARVESTING) {
      throw new InvalidCompleteException();
    }

    ZonedDateTime now = ZonedDateTime.now(DEFAULT_ZONE_ID);
    LocalDate date = now.toLocalDate();
    cultivationCycle.setOutDate(command.note(), date);

    return toResponse(cultivationCycle, room, false);
  }

  public CultivationCycleResponse getActiveCultivationCycles(
      Long farmId, Long roomId, Long userId) {
    farmAccessValidator.validateActiveFarm(farmId);
    farmAccessValidator.validateActiveMember(farmId, userId);

    Room room = getRoomOrThrow(farmId, roomId);
    if (room.getStatus() != RoomStatus.ACTIVE) {
      throw new InvalidCultivationCycleRegisterStatusException(room.getStatus());
    }

    CultivationCycle cultivationCycle =
        cultivationCycleRepository
            .findByRoom_IdAndRoom_Farm_IdAndStatusIn(roomId, farmId, ACTIVE_CYCLE_STATUSES)
            .orElseThrow(() -> new ActiveCultivationCycleNotFoundException(roomId));

    return toResponse(cultivationCycle, room, false);
  }

  private Room getRoomOrThrow(Long farmId, Long roomId) {
    Room room =
        roomRepository
            .findByIdAndFarm_Id(roomId, farmId)
            .orElseThrow(() -> new RoomNotFoundException(roomId));

    if (room.getStatus() == RoomStatus.DELETED) {
      throw new RoomNotFoundException(roomId);
    }

    return room;
  }

  private CultivationCycleResponse toResponse(
      CultivationCycle cultivationCycle, Room room, boolean includeRoomReadings) {
    return new CultivationCycleResponse(
        cultivationCycle.getId(),
        room.getId(),
        room.getName(),
        cultivationCycle.getInDate(),
        cultivationCycle.getThinningDate(),
        cultivationCycle.getHarvestStartDate(),
        cultivationCycle.getOutDate(),
        cultivationCycle.getStatus(),
        cultivationCycle.getNote(),
        includeRoomReadings
            ? cultivationCycle.getRoomReadings().stream().map(this::toRoomReadingResponse).toList()
            : Collections.emptyList());
  }

  private RoomReadingResponse toRoomReadingResponse(RoomReading roomReading) {
    return new RoomReadingResponse(
        roomReading.getId(),
        roomReading.getRoom().getId(),
        roomReading.getCreatedAt(),
        roomReading.getTemperature(),
        roomReading.getHumidity(),
        roomReading.getCo2(),
        roomReading.getMemo());
  }
}
