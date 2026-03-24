package com.example.app.role.domain;

import com.example.app.farm.domain.Farm;
import com.example.app.role.application.command.RoleUpdateCommand;
import com.example.app.role.domain.enums.PermissionKey;
import com.example.app.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    name = "roles",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_roles_farm_key",
            columnNames = {"farm_id", "role_key"}))
@Builder
public class Role extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "farm_id")
  private Farm farm;

  @Column(name = "role_key", nullable = false, length = 50)
  private String key;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false)
  private boolean isSystem;

  @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<RolePermission> rolePermissions = new ArrayList<>();

  public void addPermission(PermissionKey permissionKey) {
    boolean exists =
        this.rolePermissions.stream()
            .anyMatch(rp -> rp.getId().getPermissionKey().equals(permissionKey));

    if (exists) {
      return;
    }

    RolePermission rolePermission =
        RolePermission.builder()
            .id(new RolePermissionId(this.id, permissionKey))
            .role(this)
            .build();

    this.rolePermissions.add(rolePermission);
  }

  public void update(RoleUpdateCommand command) {
    this.name = command.name();
  }

  public void clearPermissions() {
    this.rolePermissions.clear();
  }
}
