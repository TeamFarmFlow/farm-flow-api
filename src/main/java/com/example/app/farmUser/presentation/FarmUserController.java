package com.example.app.farmUser.presentation;

import com.example.app.farmUser.application.FarmUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "농장 멤버 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/farms/{farmId}/farmUser")
public class FarmUserController {
  private final FarmUserService farmUserService;

  //    @Operation(summary = "농장 멤버 조회")
  //    @GetMapping
  //    public ResponseEntity<PageResponse<FarmUserResponse>> getFarmUsers(@PathVariable("farmId")
  // Long farmId,
  //                                                       Authentication authentication,
  //                                                       @ParameterObject
  //                                                           @PageableDefault(size = 10, sort =
  // "createdAt", direction = Sort.Direction.DESC)
  //                                                           Pageable pageable){
  //        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
  //        Page<FarmUserResponse> page = farmUserService.getFarmUsers()
  //    }
}
