package com.example.app.role.domain;

import com.example.app.role.domain.enums.PermissionKey;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RolePermissionId implements Serializable {
  @Column(name = "role_id")
  private Long roleId;

  @Enumerated(EnumType.STRING)
  @Column(name = "permission_key", length = 80)
  private PermissionKey permissionKey;
}
