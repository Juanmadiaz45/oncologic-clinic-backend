package com.oncologic.clinic.service.user;

import com.oncologic.clinic.dto.user.request.PermissionRequestDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.update.PermissionUpdateDTO;
import com.oncologic.clinic.entity.user.Permission;

import java.util.List;

public interface PermissionService {
    PermissionResponseDTO createPermission(PermissionRequestDTO permissionDTO);

    PermissionResponseDTO updatePermission(Long id, PermissionUpdateDTO permissionDTO);

    void deletePermission(Long id);

    PermissionResponseDTO getPermissionById(Long id);

    List<PermissionResponseDTO> getAllPermissions();

    Permission getPermissionEntityById(Long id);
}
