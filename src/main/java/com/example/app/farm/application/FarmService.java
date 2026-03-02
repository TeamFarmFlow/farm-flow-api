package com.example.app.farm.application;

import com.example.app.farm.application.command.FarmRegisterCommand;
import com.example.app.farm.application.command.FarmUpdateCommand;
import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.FarmUserRepository;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.farm.presentation.dto.response.FarmDetailResponse;
import com.example.app.farm.presentation.dto.response.FarmListResponse;
import com.example.app.farm.presentation.dto.response.FarmMemberResponse;
import com.example.app.farm.presentation.dto.response.FarmRegisterResponse;
import com.example.app.farm.presentation.dto.response.FarmUpdateResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FarmService {
  private final FarmRepository farmRepository;
  private final FarmUserRepository farmUserRepository;

  @Transactional
  public FarmRegisterResponse register(FarmRegisterCommand command, Long userId) {
    Farm farm = Farm.builder().name(command.name()).status(FarmStatus.ACTIVE).build();
    farmRepository.save(farm);
    farm.addFarmUser(userId);
    return new FarmRegisterResponse(farm.getId());
  }

  public Page<FarmListResponse> getFarms(Long userId, Pageable pageable) {
    return farmRepository
        .findByUserId(userId, pageable)
        .map(
            farm ->
                new FarmListResponse(
                    farm.getId(), farm.getName(), farm.getStatus(), farm.getCreatedAt()));
  }

  public FarmDetailResponse getFarm(Long id) {
    Farm farm = farmRepository.findById(id).orElseThrow(() -> new FarmNotFoundException(id));

    List<FarmMemberResponse> members =
        farmUserRepository.findUsersByFarmId(id).stream()
            .map(u -> new FarmMemberResponse(u.getId(), u.getName(), u.getEmail(), u.getStatus()))
            .toList();

    return new FarmDetailResponse(
        farm.getId(),
        farm.getName(),
        farm.getStatus(),
        farm.getCreatedAt(),
        farm.getUpdatedAt(),
        members);
  }

  @Transactional
  public FarmUpdateResponse update(FarmUpdateCommand command, Long farmId) {
    Farm farm =
        farmRepository.findById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));
    farm.update(command.name(), command.status());
    return new FarmUpdateResponse(farm.getId(), farm.getName(), farm.getStatus());
  }

  @Transactional
  public void deleteFarm(Long farmId) {
    Farm farm =
        farmRepository.findById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));
    farm.delete();
  }
}
