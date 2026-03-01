package com.example.app.farm.application;

import com.example.app.farm.application.command.FarmRegisterCommand;
import com.example.app.farm.application.command.FarmUpdateCommand;
import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.FarmUser;
import com.example.app.farm.domain.FarmUserRepository;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FarmService {
  private final FarmRepository farmRepository;
  private final FarmUserRepository farmUserRepository;
  private final UserService userService;

  @Transactional
  public Farm register(FarmRegisterCommand command, Long userId) {
    Farm farm = Farm.builder().name(command.name()).status(FarmStatus.ACTIVE).build();
    farmRepository.save(farm);

    FarmUser farmUser = new FarmUser(farm, userService.getUserById(userId));
    farmUserRepository.save(farmUser);

    return farm;
  }

  @Transactional
  public Farm update(FarmUpdateCommand command, Long farmId) {
    Farm farm =
        farmRepository.findById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));
    farm.update(command.name(), command.status());
    return farm;
  }

  @Transactional
  public void deleteFarm(Long farmId) {
    Farm farm =
        farmRepository.findById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));
    farm.delete();
  }
}
