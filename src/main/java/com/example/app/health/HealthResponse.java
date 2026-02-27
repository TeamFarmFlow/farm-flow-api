package com.example.app.health;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HealthResponse {
  private String name;
  private String version;
  private String env;
}
