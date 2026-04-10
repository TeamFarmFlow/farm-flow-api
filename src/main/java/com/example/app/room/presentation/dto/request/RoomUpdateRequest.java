package com.example.app.room.presentation.dto.request;

import com.example.app.room.application.command.RoomUpdateCommand;
import com.example.app.room.domain.enums.RoomStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomUpdateRequest {
  @NotBlank(message = "생육동 이름은 필수 입력값입니다.")
  private String name;

  private String description;

  @NotNull(message = "생육동 상태 설정은 필수 입력값입니다.")
  private RoomStatus status;

  public RoomUpdateCommand toCommand() {
    return new RoomUpdateCommand(name, description, status);
  }
}
