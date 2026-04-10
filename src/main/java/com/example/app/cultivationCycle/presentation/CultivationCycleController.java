package com.example.app.cultivationCycle.presentation;

import com.example.app.cultivationCycle.application.CultivationCycleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "생육동 사이클 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("{farmId}/{roomId}/cultivationCycle")
public class CultivationCycleController {
    private final CultivationCycleService cultivationCycleService;
}
