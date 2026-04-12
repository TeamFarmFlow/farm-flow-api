package com.example.app.roomReading.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomReadingRepository extends JpaRepository<RoomReading, Long> {
  Optional<RoomReading> findByIdAndRoom_Id(Long id, Long roomId);
}
