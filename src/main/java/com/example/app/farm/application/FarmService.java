package com.example.app.farm.application;

import org.springframework.stereotype.Service;

import com.example.app.farm.domain.FarmRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FarmService {
  private final FarmRepository farmRepository;
}
