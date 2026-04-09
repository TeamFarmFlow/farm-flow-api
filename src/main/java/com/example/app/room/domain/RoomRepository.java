package com.example.app.room.domain;
import java.util.List;
import java.util.Optional;

import com.example.app.room.domain.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
  boolean existsByFarm_IdAndName(Long farmId, String name);

  boolean existsByFarm_IdAndNameAndIdNot(Long farmId, String name, Long id);

  Optional<Room> findByIdAndFarm_Id(Long id, Long farmId);

  List<Room> findAllByFarm_IdAndStatusIn(Long farmId, List<RoomStatus> statuses);
}
