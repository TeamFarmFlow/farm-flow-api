package com.example.app.cultivationCycle.application;

import com.example.app.cultivationCycle.application.command.CultivationCycleMarkThinningCommand;
import com.example.app.cultivationCycle.domain.CultivationCycle;
import com.example.app.cultivationCycle.domain.CultivationCycleRepository;
import com.example.app.cultivationCycle.domain.enums.CultivationCycleStatus;
import com.example.app.cultivationCycle.domain.exception.AlreadyExistsCultivationCycleException;
import com.example.app.cultivationCycle.domain.exception.CultivationCycleNotFoundException;
import com.example.app.cultivationCycle.domain.exception.InvalidCultivationCycleRegisterStatusException;
import com.example.app.cultivationCycle.domain.exception.InvalidMarkThinningException;
import com.example.app.cultivationCycle.presentation.dto.response.CultivationCycleResponse;
import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.farmUser.domain.FarmUserRepository;
import com.example.app.farmUser.domain.enums.FarmUserStatus;
import com.example.app.farmUser.domain.exception.FarmUserNotFoundException;
import com.example.app.farmUser.domain.exception.MemberPermissionDeniedException;
import com.example.app.role.domain.enums.PermissionKey;
import com.example.app.room.domain.Room;
import com.example.app.room.domain.RoomRepository;
import com.example.app.room.domain.enums.RoomStatus;
import com.example.app.room.domain.exception.RoomNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
  private final FarmRepository farmRepository;
  private final RoomRepository roomRepository;
  private final FarmUserRepository farmUserRepository;

  @Transactional
  public CultivationCycleResponse register(Long farmId, Long roomId, Long userId) {
    validateFarmExists(farmId);

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

    return new CultivationCycleResponse(
        cultivationCycle.getId(),
        room.getId(),
        room.getName(),
        cultivationCycle.getInDate(),
        cultivationCycle.getThinningDate(),
        cultivationCycle.getHarvestStartDate(),
        cultivationCycle.getOutDate(),
        cultivationCycle.getStatus(),
        cultivationCycle.getNote());
  }

  public CultivationCycleResponse getCycle(Long id, Long farmId, Long roomId, Long userId) {
    validateFarmExists(farmId);
    validateMember(farmId, userId);

    Room room = getRoomOrThrow(farmId, roomId);
    CultivationCycle cultivationCycle =
        cultivationCycleRepository
            .findByIdAndRoom_IdAndRoom_Farm_Id(id, roomId, farmId)
            .orElseThrow(() -> new CultivationCycleNotFoundException(id));

    return toResponse(cultivationCycle, room);
  }

  public List<CultivationCycleResponse> getCultivationCycles(
          Long farmId, Long roomId, Long userId) {
    validateFarmExists(farmId);
    validateMember(farmId, userId);

    Room room = getRoomOrThrow(farmId, roomId);

    List<CultivationCycle> cultivationCycles =
        cultivationCycleRepository.findAllByRoom_IdOrderByInDateDescIdDesc(roomId);

    return cultivationCycles.stream()
        .map(cultivationCycle -> toResponse(cultivationCycle, room))
        .toList();
  }

    @Transactional
    public CultivationCycleResponse markThinning(CultivationCycleMarkThinningCommand command, Long farmId, Long roomId, Long id, Long userId) {
        validateFarmExists(farmId);

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

        if(cultivationCycle.getStatus() != CultivationCycleStatus.IN_PROGRESS) {
            throw new InvalidMarkThinningException();
        }
        ZonedDateTime now = ZonedDateTime.now(DEFAULT_ZONE_ID);
        LocalDate date = now.toLocalDate();
        cultivationCycle.setThinningDate(command.note(), date);

        return toResponse(cultivationCycle, room);
    }

  private void validateFarmExists(Long farmId) {
    farmRepository
        .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
        .orElseThrow(() -> new FarmNotFoundException(farmId));
  }

  private void validateMember(Long farmId, Long userId) {
    boolean isMember =
        farmUserRepository.existsByFarm_IdAndUser_IdAndStatus(
            farmId, userId, FarmUserStatus.ACTIVE);
    if (!isMember) {
      throw new FarmUserNotFoundException(userId);
    }
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

  private CultivationCycleResponse toResponse(CultivationCycle cultivationCycle, Room room) {
    return new CultivationCycleResponse(
        cultivationCycle.getId(),
        room.getId(),
        room.getName(),
        cultivationCycle.getInDate(),
        cultivationCycle.getThinningDate(),
        cultivationCycle.getHarvestStartDate(),
        cultivationCycle.getOutDate(),
        cultivationCycle.getStatus(),
        cultivationCycle.getNote());
  }
}
