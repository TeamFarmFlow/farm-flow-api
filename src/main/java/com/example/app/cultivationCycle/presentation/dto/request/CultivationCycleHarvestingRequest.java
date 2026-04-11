package com.example.app.cultivationCycle.presentation.dto.request;

import com.example.app.cultivationCycle.application.command.CultivationCycleHarvestingCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CultivationCycleHarvestingRequest {
  private String note;

  public CultivationCycleHarvestingCommand toCommand() {
    return new CultivationCycleHarvestingCommand(note);
  }
}
