package com.example.app.roomReading.domain;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomReadingRepository extends JpaRepository<RoomReading, Long> {
  Optional<RoomReading> findByIdAndRoom_Id(Long id, Long roomId);

  @Query(
      """
      select rr
      from RoomReading rr
      where rr.room.farm.id = :farmId
        and (:roomId is null or rr.room.id = :roomId)
        and (:from is null or rr.createdAt >= :from)
        and (:toExclusive is null or rr.createdAt < :toExclusive)
      order by rr.createdAt desc, rr.id desc
      """)
  List<RoomReading> findAllByFilter(
      @Param("farmId") Long farmId,
      @Param("roomId") Long roomId,
      @Param("from") Instant from,
      @Param("toExclusive") Instant toExclusive);
}
