package com.example.app.cultivationCycle.domain;

import com.example.app.cultivationCycle.domain.enums.CultivationCycleStatus;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CultivationCycleRepository extends JpaRepository<CultivationCycle, Long> {
  boolean existsByRoomIdAndStatusIn(Long roomId, Collection<CultivationCycleStatus> statuses);

  List<CultivationCycle> findAllByRoom_IdOrderByInDateDescIdDesc(Long roomId);

  Optional<CultivationCycle> findByIdAndRoom_IdAndRoom_Farm_Id(Long id, Long roomId, Long farmId);

  Optional<CultivationCycle> findByRoom_IdAndRoom_Farm_IdAndStatusIn(
      Long roomId, Long farmId, List<CultivationCycleStatus> activeCycleStatuses);
}
