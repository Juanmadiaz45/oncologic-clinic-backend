package com.oncologic.clinic.service.user;

import com.oncologic.clinic.entity.user.Permission;

import java.util.List;

public interface PermissionService {
    Permission createPermission(Permission permission);
    Permission updatePermission(Permission permission);
    void deletePermission(Long id);
    Permission getPermissionById(Long id);
    List<Permission> getAllPermissions();
}
