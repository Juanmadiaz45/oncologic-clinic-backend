package com.oncologic.clinic.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PERMISSIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermission> rolePermissions = new HashSet<>();
}

