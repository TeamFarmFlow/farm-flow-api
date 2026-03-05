package com.example.app.farm.application;

import com.example.app.farm.application.command.FarmRegisterCommand;
import com.example.app.farm.application.command.FarmUpdateCommand;
import com.example.app.farm.domain.*;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farm.domain.enums.FarmUserStatus;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.farm.presentation.dto.response.FarmDetailResponse;
import com.example.app.farm.presentation.dto.response.FarmListResponse;
import com.example.app.farm.presentation.dto.response.FarmMemberResponse;
import com.example.app.farm.presentation.dto.response.FarmRegisterResponse;
import com.example.app.farm.presentation.dto.response.FarmUpdateResponse;
import java.util.List;
import java.util.Map;

import com.example.app.role.application.RoleSeedService;
import com.example.app.role.domain.Role;
import com.example.app.user.domain.User;
import com.example.app.user.domain.UserRepository;
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
  private final UserRepository userRepository;
  private final RoleSeedService roleSeedService;

  @Transactional
  public FarmRegisterResponse register(FarmRegisterCommand command, Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new FarmNotFoundException(userId));

    Farm farm = Farm.builder().name(command.name()).status(FarmStatus.ACTIVE).build();
    farmRepository.save(farm);

    Map<String, Role> roleMap = roleSeedService.seed(farm);

    FarmUser farmUser = FarmUser.builder()
            .id(new FarmUserId(farm.getId(), user.getId()))
            .farm(farm)
            .user(user)
            .role(roleMap.get("OWNER"))
            .status(FarmUserStatus.ACTIVE)
            .build();

    farmUserRepository.save(farmUser);

    return new FarmRegisterResponse(farm.getId());
  }

  public Page<FarmListResponse> getFarms(Long userId, Pageable pageable) {
    return farmRepository
        .findAllByUserId(userId, pageable)
        .map(
            farm ->
                new FarmListResponse(
                    farm.getId(), farm.getName(), farm.getStatus(), farm.getCreatedAt()));
  }

  public FarmDetailResponse getFarm(Long id, Long userId) {
    Farm farm =
        farmRepository.findByUserId(id, userId).orElseThrow(() -> new FarmNotFoundException(id));

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
  public FarmUpdateResponse update(FarmUpdateCommand command, Long id, Long userId) {
    Farm farm =
        farmRepository.findByUserId(id, userId).orElseThrow(() -> new FarmNotFoundException(id));
    farm.update(command.name(), command.status());
    return new FarmUpdateResponse(farm.getId(), farm.getName(), farm.getStatus());
  }

  @Transactional
  public void deleteFarm(Long id, Long userId) {
    Farm farm =
        farmRepository.findByUserId(id, userId).orElseThrow(() -> new FarmNotFoundException(id));
    farm.delete();
  }
}
