package com.example.FarmFlow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HealthDTO {
  private String name;
  private String version;
  private String env;
}
