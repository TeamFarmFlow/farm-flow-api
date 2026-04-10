package com.example.app.room.presentation.dto.request;

import com.example.app.room.application.command.RoomRegisterCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomRegisterRequest {
  @NotBlank(message = "생육동 이름은 필수 입력값입니다.")
  private String name;

  private String description;

  public RoomRegisterCommand toCommand() {
    return new RoomRegisterCommand(name, description);
  }
}
