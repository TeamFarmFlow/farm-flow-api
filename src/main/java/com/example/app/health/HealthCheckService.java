package com.example.app.health;

import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {
  public HealthResponse healthCheck() {
    return HealthResponse.builder().name("farm-flow-api").version("0.0.1").env("local").build();
  }
}
