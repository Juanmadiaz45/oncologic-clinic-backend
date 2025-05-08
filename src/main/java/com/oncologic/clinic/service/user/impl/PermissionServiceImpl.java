package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.dto.user.request.PermissionRequestDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.update.PermissionUpdateDTO;
import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.mapper.user.PermissionMapper;
import com.oncologic.clinic.repository.user.RolePermissionRepository;
import com.oncologic.clinic.service.user.PermissionService;
import jakarta.persistence.EntityNotFoundException;
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
    public PermissionResponseDTO createPermission(PermissionRequestDTO permissionDTO) {
        if (permissionDTO == null) {
            throw new IllegalArgumentException("El permiso no puede ser nulo");
        }
        if (permissionDTO.getName() == null || permissionDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del permiso no puede ser nulo o vacío");
        }

        // Verifica si el permiso ya existe
        if (permissionRepository.existsByName(permissionDTO.getName())) {
            throw new IllegalArgumentException("El permiso ya existe");
        }

        Permission permission = permissionMapper.permissionRequestDtoToPermission(permissionDTO);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.permissionToPermissionResponseDto(savedPermission);
    }

    @Override
    public PermissionResponseDTO updatePermission(Long id, PermissionUpdateDTO permissionDTO) {
        Permission existingPermission = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado con ID: " + id));

        permissionMapper.updatePermissionFromDto(permissionDTO, existingPermission);
        Permission updatedPermission = permissionRepository.save(existingPermission);

        return permissionMapper.permissionToPermissionResponseDto(updatedPermission);
    }

    @Override
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado con ID: " + id));

        // Verifica si el permiso está asignado a algún rol
        boolean isAssignedToRole = rolePermissionRepository.existsByPermissionId(id);

        if (isAssignedToRole) {
            throw new IllegalStateException("No se puede eliminar el permiso porque está asignado a roles");
        }

        permissionRepository.delete(permission);
    }

    @Override
    public PermissionResponseDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado con ID: " + id));

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
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado con ID: " + id));
    }
}