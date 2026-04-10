package com.example.app.attendance.presentation.dto.request;

import com.example.app.attendance.application.command.AttendanceUpdateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttendanceUpdateRequest {
  @NotNull(message = "출근 시간은 필수 입력값입니다.")
  private LocalDateTime clockInAt;

  private LocalDateTime clockOutAt;

  @NotBlank(message = "수정 사유는 필수 입력값입니다.")
  private String reason;

  public AttendanceUpdateCommand toCommand() {
    return new AttendanceUpdateCommand(clockInAt, clockOutAt, reason);
  }
}
