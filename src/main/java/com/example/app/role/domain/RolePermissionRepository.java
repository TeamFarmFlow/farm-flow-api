package com.example.app.role.domain;

import com.example.app.role.domain.enums.PermissionKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

}
