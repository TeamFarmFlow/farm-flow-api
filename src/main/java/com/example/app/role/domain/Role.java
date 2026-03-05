package com.example.app.role.domain;

import com.example.app.shared.entity.BaseTimeEntity;
import com.example.app.farm.domain.Farm;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "roles",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_roles_farm_key",
                columnNames = {"farm_id", "role_key"}
        )
)
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
}
