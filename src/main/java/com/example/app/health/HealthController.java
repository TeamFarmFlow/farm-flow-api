package com.example.app.health;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {
  private final HealthCheckService healthCheckService;

  @GetMapping()
  public HealthResponse healthCheck() {
    return healthCheckService.healthCheck();
  }
}
