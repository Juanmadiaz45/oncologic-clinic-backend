package com.oncologic.clinic.service.user;

import com.oncologic.clinic.dto.user.PermissionDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.entity.user.Permission;

import java.util.List;

public interface PermissionService {
    PermissionResponseDTO createPermission(PermissionDTO permissionDTO);

    PermissionResponseDTO updatePermission(Long id, PermissionDTO permissionDTO);

    void deletePermission(Long id);

    PermissionResponseDTO getPermissionById(Long id);

    List<PermissionResponseDTO> getAllPermissions();

    Permission getPermissionEntityById(Long id);
}
