package com.example.app.farm.presentation.dto.request;

import com.example.app.farm.application.command.FarmRegisterCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FarmRegisterRequest {
  @NotBlank(message = "농장 이름은 필수 입력값입니다.")
  private String name;

  public FarmRegisterCommand toCommand() {
    return new FarmRegisterCommand(name);
  }
}
