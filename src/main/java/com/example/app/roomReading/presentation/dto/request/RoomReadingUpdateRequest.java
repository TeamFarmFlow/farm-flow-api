package com.example.app.roomReading.presentation.dto.request;

import com.example.app.roomReading.application.command.RoomReadingUpdateCommand;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomReadingUpdateRequest {
  private Long cultivationCycle_id;

  @NotNull(message = "온도 값은 필수 입력값 입니다.")
  private BigDecimal temperature;

  @NotNull(message = "습도 값은 필수 입력값 입니다.")
  private BigDecimal humidity;

  @NotNull(message = "Co2 값은 필수 입력값 입니다.")
  private BigDecimal co2;

  private String memo;

  public RoomReadingUpdateCommand toCommand() {
    return new RoomReadingUpdateCommand(cultivationCycle_id, temperature, humidity, co2, memo);
  }
}
