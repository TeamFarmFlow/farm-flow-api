package com.example.app.room.application;

import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.farmUser.domain.FarmUserRepository;
import com.example.app.farmUser.domain.enums.FarmUserStatus;
import com.example.app.farmUser.domain.exception.FarmUserNotFoundException;
import com.example.app.farmUser.domain.exception.MemberPermissionDeniedException;
import com.example.app.role.domain.enums.PermissionKey;
import com.example.app.room.application.command.RoomRegisterCommand;
import com.example.app.room.application.command.RoomUpdateCommand;
import com.example.app.room.domain.Room;
import com.example.app.room.domain.RoomRepository;
import com.example.app.room.domain.enums.RoomStatus;
import com.example.app.room.domain.exception.AlreadyRoomDeletedException;
import com.example.app.room.domain.exception.InvalidRoomStatusException;
import com.example.app.room.domain.exception.RoomNameAlreadyExistsException;
import com.example.app.room.domain.exception.RoomNotFoundException;
import com.example.app.room.presentation.dto.response.RoomResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
  private final RoomRepository roomRepository;
  private final FarmRepository farmRepository;
  private final FarmUserRepository farmUserRepository;

  @Transactional
  public RoomResponse register(Long farmId, RoomRegisterCommand command, Long userId) {
    String normalizedName = command.name().trim();

    Farm farm =
        farmRepository
            .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
            .orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean isManageRoom =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.ROOM_MANAGE);
    if (!isManageRoom) {
      throw new MemberPermissionDeniedException();
    }

    boolean existsRoomName = roomRepository.existsByFarm_IdAndName(farmId, normalizedName);
    if (existsRoomName) {
      throw new RoomNameAlreadyExistsException(normalizedName);
    }

    Room room = Room.create(farm, normalizedName, command.description());
    try {
      roomRepository.save(room);
    } catch (DataIntegrityViolationException e) {
      throw new RoomNameAlreadyExistsException(normalizedName);
    }

    return new RoomResponse(
        room.getId(),
        room.getName(),
        room.getDescription(),
        room.getStatus(),
        room.getCreatedAt(),
        room.getUpdatedAt(),
        room.getDeletedAt());
  }

  public RoomResponse getRoom(Long farmId, Long userId, Long id) {
    farmRepository
        .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
        .orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean isMember =
        farmUserRepository.existsByFarm_IdAndUser_IdAndStatus(
            farmId, userId, FarmUserStatus.ACTIVE);
    if (!isMember) {
      throw new FarmUserNotFoundException(userId);
    }

    Room room =
        roomRepository
            .findByIdAndFarm_Id(id, farmId)
            .orElseThrow(() -> new RoomNotFoundException(id));
    if (room.getStatus() == RoomStatus.DELETED) {
      throw new RoomNotFoundException(id);
    }

    return new RoomResponse(
        room.getId(),
        room.getName(),
        room.getDescription(),
        room.getStatus(),
        room.getCreatedAt(),
        room.getUpdatedAt(),
        room.getDeletedAt());
  }

  public List<RoomResponse> getAllRooms(Long farmId, Long userId) {
    farmRepository
        .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
        .orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean isMember =
        farmUserRepository.existsByFarm_IdAndUser_IdAndStatus(
            farmId, userId, FarmUserStatus.ACTIVE);
    if (!isMember) {
      throw new FarmUserNotFoundException(userId);
    }

    List<Room> rooms =
        roomRepository.findAllByFarm_IdAndStatusIn(
            farmId, List.of(RoomStatus.ACTIVE, RoomStatus.INACTIVE));

    return rooms.stream()
        .map(
            room ->
                new RoomResponse(
                    room.getId(),
                    room.getName(),
                    room.getDescription(),
                    room.getStatus(),
                    room.getCreatedAt(),
                    room.getUpdatedAt(),
                    room.getDeletedAt()))
        .toList();
  }

  @Transactional
  public RoomResponse updateRoom(Long farmId, Long id, RoomUpdateCommand command, Long userId) {
    farmRepository
        .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
        .orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean isManageRoom =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.ROOM_MANAGE);
    if (!isManageRoom) {
      throw new MemberPermissionDeniedException();
    }

    Room room =
        roomRepository
            .findByIdAndFarm_Id(id, farmId)
            .orElseThrow(() -> new RoomNotFoundException(id));
    if (room.getStatus() == RoomStatus.DELETED) {
      throw new RoomNotFoundException(id);
    }

    String normalizedName = command.name().trim();
    if (command.status() == RoomStatus.DELETED) {
      throw new InvalidRoomStatusException();
    }

    boolean existsRoomName =
        roomRepository.existsByFarm_IdAndNameAndIdNot(farmId, normalizedName, room.getId());
    if (existsRoomName) {
      throw new RoomNameAlreadyExistsException(normalizedName);
    }

    room.update(command.name(), command.description(), command.status());

    return new RoomResponse(
        room.getId(),
        room.getName(),
        room.getDescription(),
        room.getStatus(),
        room.getCreatedAt(),
        room.getUpdatedAt(),
        room.getDeletedAt());
  }

  @Transactional
  public void deleteRoom(Long id, Long farmId, Long userId) {
    farmRepository
        .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
        .orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean isManageRoom =
        farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
            farmId, userId, FarmUserStatus.ACTIVE, PermissionKey.ROOM_MANAGE);
    if (!isManageRoom) {
      throw new MemberPermissionDeniedException();
    }

    Room room =
        roomRepository
            .findByIdAndFarm_Id(id, farmId)
            .orElseThrow(() -> new RoomNotFoundException(id));

    if (room.getStatus().equals(RoomStatus.DELETED)) {
      throw new AlreadyRoomDeletedException(id);
    }

    room.delete();
  }
}
