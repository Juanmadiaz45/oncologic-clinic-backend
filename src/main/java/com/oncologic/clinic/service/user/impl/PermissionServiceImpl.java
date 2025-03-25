package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.service.user.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oncologic.clinic.repository.user.PermissionRepository;

import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission createPermission(Permission permission) {
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
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permiso no encontrado"));

        if (!permission.getRoles().isEmpty()) {
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
