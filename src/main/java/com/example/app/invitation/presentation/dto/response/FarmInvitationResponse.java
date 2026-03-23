package com.example.app.invitation.presentation.dto.response;

import com.example.app.invitation.domain.enums.FarmInvitationStatus;
import com.example.app.role.domain.Role;
import com.example.app.user.domain.User;

import java.time.Instant;

public record FarmInvitationResponse(Long id,
                                     String inviteeEmail,
                                     String assignedRoleName,
                                     FarmInvitationStatus status,
                                     Instant respondedAt,
                                     Long inviterId,
                                     String inviterName,
                                     Long inviteeUserId,
                                     String inviteeUserName,
                                     Instant createdAt,
                                     Instant updatedAt){
}
