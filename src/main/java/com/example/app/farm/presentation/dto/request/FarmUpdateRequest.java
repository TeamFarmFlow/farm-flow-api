package com.example.app.farm.presentation.dto.request;

import com.example.app.farm.application.command.FarmUpdateCommand;
import com.example.app.farm.domain.enums.FarmStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FarmUpdateRequest {
  @NotBlank(message = "농장 이름은 필수 입력값입니다.")
  private String name;

  private FarmStatus status;

  public FarmUpdateCommand toCommand() {
    return new FarmUpdateCommand(name, status);
  }
}
