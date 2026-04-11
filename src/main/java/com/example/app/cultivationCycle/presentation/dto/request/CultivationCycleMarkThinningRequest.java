package com.example.app.cultivationCycle.presentation.dto.request;

import com.example.app.cultivationCycle.application.command.CultivationCycleMarkThinningCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CultivationCycleMarkThinningRequest {
  private String note;

  public CultivationCycleMarkThinningCommand toCommand() {
    return new CultivationCycleMarkThinningCommand(note);
  }
}
