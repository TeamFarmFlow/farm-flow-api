package com.example.app.roomReading.application;

import com.example.app.cultivationCycle.domain.CultivationCycle;
import com.example.app.cultivationCycle.domain.CultivationCycleRepository;
import com.example.app.cultivationCycle.domain.enums.CultivationCycleStatus;
import com.example.app.cultivationCycle.domain.exception.CultivationCycleNotFoundException;
import com.example.app.farm.application.FarmAccessValidator;
import com.example.app.farmUser.domain.FarmUserRepository;
import com.example.app.farmUser.domain.enums.FarmUserStatus;
import com.example.app.farmUser.domain.exception.MemberPermissionDeniedException;
import com.example.app.role.domain.enums.PermissionKey;
import com.example.app.room.domain.Room;
import com.example.app.room.domain.RoomRepository;
import com.example.app.room.domain.enums.RoomStatus;
import com.example.app.room.domain.exception.InvalidRoomStatusException;
import com.example.app.room.domain.exception.RoomNotFoundException;
import com.example.app.roomReading.application.command.RoomReadingRegisterCommand;
import com.example.app.roomReading.application.command.RoomReadingUpdateCommand;
import com.example.app.roomReading.domain.RoomReading;
import com.example.app.roomReading.domain.RoomReadingRepository;
import com.example.app.roomReading.domain.exception.RoomReadingNotFoundException;
import com.example.app.roomReading.presentation.dto.response.RoomReadingResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomReadingService {
  private static final List<CultivationCycleStatus> ACTIVE_CYCLE_STATUSES =
      List.of(
          CultivationCycleStatus.IN_PROGRESS,
          CultivationCycleStatus.THINNED,
          CultivationCycleStatus.HARVESTING);

  private final RoomReadingRepository roomReadingRepository;
  private final FarmAccessValidator farmAccessValidator;
  private final RoomRepository roomRepository;
  private final FarmUserRepository farmUserRepository;
  private final CultivationCycleRepository cultivationCycleRepository;

  @Transactional
  public RoomReadingResponse register(
      Long farmId, Long roomId, Long userId, RoomReadingRegisterCommand command) {
    farmAccessValidator.validateActiveFarm(farmId);

    boolean isManageRoomReading =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.ROOM_READING_CREATE);
    if (!isManageRoomReading) {
      throw new MemberPermissionDeniedException();
    }

    Room room =
        roomRepository
            .findByIdAndFarm_Id(roomId, farmId)
            .orElseThrow(() -> new RoomNotFoundException(roomId));
    if (room.getStatus() != RoomStatus.ACTIVE) {
      throw new InvalidRoomStatusException();
    }

    CultivationCycle cultivationCycle =
        cultivationCycleRepository
            .findByRoom_IdAndRoom_Farm_IdAndStatusIn(roomId, farmId, ACTIVE_CYCLE_STATUSES)
            .orElse(null);

    RoomReading roomReading =
        RoomReading.create(
            room,
            cultivationCycle,
            command.temperature(),
            command.humidity(),
            command.co2(),
            command.memo());

    roomReadingRepository.save(roomReading);

    return new RoomReadingResponse(
        roomReading.getId(),
        roomReading.getCreatedAt(),
        roomReading.getTemperature(),
        roomReading.getHumidity(),
        roomReading.getCo2(),
        roomReading.getMemo());
  }

  public RoomReadingResponse getRoomReading(Long id, Long farmId, Long roomId, Long userId) {
    farmAccessValidator.validateActiveFarm(farmId);
    farmAccessValidator.validateActiveMember(farmId, userId);
    Room room =
        roomRepository
            .findByIdAndFarm_Id(roomId, farmId)
            .orElseThrow(() -> new RoomNotFoundException(roomId));
    if (room.getStatus() == RoomStatus.DELETED) {
      throw new InvalidRoomStatusException();
    }

    RoomReading roomReading =
        roomReadingRepository
            .findByIdAndRoom_Id(id, roomId)
            .orElseThrow(() -> new RoomReadingNotFoundException(id));

    return new RoomReadingResponse(
        roomReading.getId(),
        roomReading.getCreatedAt(),
        roomReading.getTemperature(),
        roomReading.getHumidity(),
        roomReading.getCo2(),
        roomReading.getMemo());
  }

  @Transactional
  public void deleteRoomReading(Long id, Long farmId, Long roomId, Long userId) {
    farmAccessValidator.validateActiveFarm(farmId);

    boolean isManageRoomReading =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.ROOM_READING_DELETE);
    if (!isManageRoomReading) {
      throw new MemberPermissionDeniedException();
    }

    Room room =
        roomRepository
            .findByIdAndFarm_Id(roomId, farmId)
            .orElseThrow(() -> new RoomNotFoundException(roomId));
    if (room.getStatus() != RoomStatus.ACTIVE) {
      throw new InvalidRoomStatusException();
    }

    RoomReading roomReading =
        roomReadingRepository
            .findByIdAndRoom_Id(id, roomId)
            .orElseThrow(() -> new RoomReadingNotFoundException(id));

    roomReadingRepository.delete(roomReading);
  }

  @Transactional
  public RoomReadingResponse updateRoomReading(
      Long id, Long farmId, Long roomId, Long userId, RoomReadingUpdateCommand command) {
    farmAccessValidator.validateActiveFarm(farmId);

    boolean isManageRoomReading =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.ROOM_READING_UPDATE);
    if (!isManageRoomReading) {
      throw new MemberPermissionDeniedException();
    }

    Room room =
        roomRepository
            .findByIdAndFarm_Id(roomId, farmId)
            .orElseThrow(() -> new RoomNotFoundException(roomId));
    if (room.getStatus() != RoomStatus.ACTIVE) {
      throw new InvalidRoomStatusException();
    }

    RoomReading roomReading =
        roomReadingRepository
            .findByIdAndRoom_Id(id, roomId)
            .orElseThrow(() -> new RoomReadingNotFoundException(id));

    CultivationCycle cycle = null;
    if (command.cultivationCycle_id() != null) {
      cycle =
          cultivationCycleRepository
              .findByIdAndRoom_IdAndRoom_Farm_Id(command.cultivationCycle_id(), roomId, farmId)
              .orElseThrow(
                  () -> new CultivationCycleNotFoundException(command.cultivationCycle_id()));
    }
    roomReading.update(
        cycle, command.temperature(), command.humidity(), command.co2(), command.memo());

    return new RoomReadingResponse(
        roomReading.getId(),
        roomReading.getCreatedAt(),
        roomReading.getTemperature(),
        roomReading.getHumidity(),
        roomReading.getCo2(),
        roomReading.getMemo());
  }
}
