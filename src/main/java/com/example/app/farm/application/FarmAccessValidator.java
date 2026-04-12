package com.example.app.farm.application;

import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.farmUser.domain.FarmUserRepository;
import com.example.app.farmUser.domain.enums.FarmUserStatus;
import com.example.app.farmUser.domain.exception.FarmUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FarmAccessValidator {
  private final FarmRepository farmRepository;
  private final FarmUserRepository farmUserRepository;

  public Farm getActiveFarm(Long farmId) {
    return farmRepository
        .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
        .orElseThrow(() -> new FarmNotFoundException(farmId));
  }

  public void validateActiveFarm(Long farmId) {
    getActiveFarm(farmId);
  }

  public void validateActiveMember(Long farmId, Long userId) {
    boolean isMember =
        farmUserRepository.existsByFarm_IdAndUser_IdAndStatus(
            farmId, userId, FarmUserStatus.ACTIVE);
    if (!isMember) {
      throw new FarmUserNotFoundException(userId);
    }
  }
}
