package com.example.app.roomReading.presentation.dto.request;

import com.example.app.roomReading.application.command.RoomReadingRegisterCommand;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomReadingRegisterRequest {
  @NotNull(message = "온도 값은 필수 입력값 입니다.")
  private BigDecimal temperature;

  @NotNull(message = "습도 값은 필수 입력값 입니다.")
  private BigDecimal humidity;

  @NotNull(message = "Co2 값은 필수 입력값 입니다.")
  private BigDecimal co2;

  private String memo;

  public RoomReadingRegisterCommand toCommand() {
    return new RoomReadingRegisterCommand(temperature, humidity, co2, memo);
  }
}
