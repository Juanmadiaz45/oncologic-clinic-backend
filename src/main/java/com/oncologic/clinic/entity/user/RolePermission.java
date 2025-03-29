package com.oncologic.clinic.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Entity
@Table(name = "ROLE_PERMISSIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission {

    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RolePermissionId implements java.io.Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @Column(name = "role_id", nullable = false)
        private Long roleId;

        @Column(name = "permission_id", nullable = false)
        private Long permissionId;
    }
}