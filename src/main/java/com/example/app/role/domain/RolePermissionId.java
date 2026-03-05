package com.example.app.role.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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

  @Column(name = "permission_key", length = 80)
  private String permissionKey;
}
