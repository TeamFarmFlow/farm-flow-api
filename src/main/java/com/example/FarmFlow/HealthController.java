package com.example.FarmFlow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
  @GetMapping()
  public HealthDTO healthCheck() {
    return HealthDTO.builder()
        .name("farm-flow-api")
        .version("0.0.1")
        .env("local")
        .build();
  }
}
