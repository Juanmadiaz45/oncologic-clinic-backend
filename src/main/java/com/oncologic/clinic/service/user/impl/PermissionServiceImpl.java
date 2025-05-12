package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.dto.user.PermissionDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.exception.runtime.user.PermissionNotFoundException;
import com.oncologic.clinic.mapper.user.PermissionMapper;
import com.oncologic.clinic.repository.user.RolePermissionRepository;
import com.oncologic.clinic.service.user.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oncologic.clinic.repository.user.PermissionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository,
            PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public PermissionResponseDTO createPermission(PermissionDTO permissionDTO) {
        if (permissionDTO == null) {
            throw new IllegalArgumentException("The permit cannot be void");
        }
        if (permissionDTO.getName() == null || permissionDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("The permission name cannot be null or empty");
        }

        // Check if the permit already exists
        if (permissionRepository.existsByName(permissionDTO.getName())) {
            throw new IllegalArgumentException("The permit already exists");
        }

        Permission permission = permissionMapper.permissionDtoToPermission(permissionDTO);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.permissionToPermissionResponseDto(savedPermission);
    }

    @Override
    public PermissionResponseDTO updatePermission(Long id, PermissionDTO permissionDTO) {
        Permission existingPermission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionNotFoundException(id));

        permissionMapper.updatePermissionFromDto(permissionDTO, existingPermission);
        Permission updatedPermission = permissionRepository.save(existingPermission);

        return permissionMapper.permissionToPermissionResponseDto(updatedPermission);
    }

    @Override
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionNotFoundException(id));

        // Check if the permission is assigned to any role
        boolean isAssignedToRole = rolePermissionRepository.existsByPermissionId(id);

        if (isAssignedToRole) {
            throw new IllegalStateException("The permission cannot be removed because it is assigned to roles");
        }

        permissionRepository.delete(permission);
    }

    @Override
    public PermissionResponseDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionNotFoundException(id));

        return permissionMapper.permissionToPermissionResponseDto(permission);
    }

    @Override
    public List<PermissionResponseDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::permissionToPermissionResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Permission getPermissionEntityById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionNotFoundException(id));
    }
}