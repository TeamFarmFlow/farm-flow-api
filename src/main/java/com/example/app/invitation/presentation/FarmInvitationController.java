package com.example.app.invitation.presentation;

import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.invitation.application.FarmInvitationService;
import com.example.app.invitation.presentation.dto.request.FarmInvitationAcceptRequest;
import com.example.app.invitation.presentation.dto.request.FarmInvitationRegisterRequest;
import com.example.app.invitation.presentation.dto.response.FarmInvitationResponse;
import com.example.app.shared.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "농장 초대")
@RestController
@RequiredArgsConstructor
@RequestMapping("/farms/{farmId}/invitations")
public class FarmInvitationController {

  private final FarmInvitationService farmInvitationService;

  @Operation(summary = "초대 생성(OWNER)")
  @PostMapping
  public void createInvitation(
      @PathVariable Long farmId,
      @RequestBody FarmInvitationRegisterRequest request,
      Authentication authentication) {

    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    farmInvitationService.createInvitation(request.toCommand(), farmId, userId);
  }

  @Operation(summary = "초대 승인")
  @PostMapping("/accept")
  public void acceptInvitation(
      @RequestBody FarmInvitationAcceptRequest request, Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    farmInvitationService.acceptInvitation(request.toCommand(), userId);
  }

  @Operation(summary = "초대 목록 조회")
  @GetMapping()
  public ResponseEntity<PageResponse<FarmInvitationResponse>> getFarmInvitations(
      Authentication authentication,
      @PathVariable Long farmId,
      @ParameterObject
          @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    Page<FarmInvitationResponse> page =
        farmInvitationService.getFarmInvitations(farmId, userId, pageable);
    return ResponseEntity.ok(PageResponse.from(page));
  }

  @Operation(summary = "초대 취소")
  @PutMapping({"{id}"})
  public void cancleInvitation(@PathVariable Long id) {
    farmInvitationService.cancleInvitation(id);
  }
}
