package com.example.app.cultivationCycle.presentation.dto.request;

import com.example.app.cultivationCycle.application.command.CultivationCycleCompleteCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CultivationCycleCompleteRequest {
  private String note;

  public CultivationCycleCompleteCommand toCommand() {
    return new CultivationCycleCompleteCommand(note);
  }
}
