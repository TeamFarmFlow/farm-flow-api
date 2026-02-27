package com.example.app.farm.application;

import com.example.app.farm.domain.FarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FarmService {
  private final FarmRepository farmRepository;
}
