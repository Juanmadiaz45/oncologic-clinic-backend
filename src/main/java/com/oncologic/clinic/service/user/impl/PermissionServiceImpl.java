package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.repository.user.RolePermissionRepository;
import com.oncologic.clinic.service.user.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oncologic.clinic.repository.user.PermissionRepository;

import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public Permission createPermission(Permission permission) {
        if (permission == null) {
            throw new IllegalArgumentException("El permiso no puede ser nulo");
        }
        if (permission.getName() == null || permission.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del permiso no puede ser nulo o vacío");
        }
        // Verifica si el permiso ya existe
        if (permissionRepository.existsByName(permission.getName())) {
            throw new IllegalArgumentException("El permiso ya existe");
        }
        return permissionRepository.save(permission);
    }

    @Override
    public Permission updatePermission(Permission permission) {
        Permission existingPermission = permissionRepository.findById(permission.getId()).orElseThrow(() -> new RuntimeException("Permiso no encontrado"));

        existingPermission.setName(permission.getName());
        return permissionRepository.save(existingPermission);
    }

    @Override
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));

        // Verifica si el permiso está asignado a algún rol a través de RolePermission
        boolean isAssignedToRole = rolePermissionRepository.existsByPermissionId(id);

        if (isAssignedToRole) {
            throw new IllegalStateException("No se puede eliminar el permiso porque está asignado a roles");
        }

        permissionRepository.delete(permission);
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

}
