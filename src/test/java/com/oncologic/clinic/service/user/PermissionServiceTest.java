package com.oncologic.clinic.service.user;

import com.oncologic.clinic.dto.user.PermissionDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.exception.runtime.user.PermissionNotFoundException;
import com.oncologic.clinic.mapper.user.PermissionMapper;
import com.oncologic.clinic.repository.user.PermissionRepository;
import com.oncologic.clinic.repository.user.RolePermissionRepository;
import com.oncologic.clinic.service.user.impl.PermissionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {
    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private PermissionServiceImpl permissionServiceImpl;

    @Test
    void createPermission_WhenValidPermissionDTO_ReturnsSavedPermissionResponseDTO() {
        // Arrange
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setName("READ_PRIVILEGES");

        Permission permission = new Permission();
        permission.setName("READ_PRIVILEGES");

        Permission savedPermission = new Permission();
        savedPermission.setId(1L);
        savedPermission.setName("READ_PRIVILEGES");

        PermissionResponseDTO responseDTO = new PermissionResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("READ_PRIVILEGES");

        when(permissionRepository.existsByName("READ_PRIVILEGES")).thenReturn(false);
        when(permissionMapper.permissionDtoToPermission(permissionDTO)).thenReturn(permission);
        when(permissionRepository.save(permission)).thenReturn(savedPermission);
        when(permissionMapper.permissionToPermissionResponseDto(savedPermission)).thenReturn(responseDTO);

        // Act
        PermissionResponseDTO result = permissionServiceImpl.createPermission(permissionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("READ_PRIVILEGES", result.getName());

        verify(permissionRepository).save(permission);
    }

    @Test
    void createPermission_WhenNullPermissionProvided_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> permissionServiceImpl.createPermission(null));
        verify(permissionRepository, never()).save(any());
    }

    @Test
    void createPermission_WhenNameIsNull_ThrowsIllegalArgumentException() {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setName(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> permissionServiceImpl.createPermission(permissionDTO));
        assertEquals("The permission name cannot be null or empty", exception.getMessage());
    }

    @Test
    void createPermission_WhenPermissionAlreadyExists_ThrowsIllegalArgumentException() {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setName("READ_PRIVILEGES");

        when(permissionRepository.existsByName("READ_PRIVILEGES")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> permissionServiceImpl.createPermission(permissionDTO));
        verify(permissionRepository, never()).save(any());
    }

    @Test
    void updatePermission_WhenPermissionExists_ReturnsUpdatedPermissionResponseDTO() {
        Long id = 1L;
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setName("WRITE_PRIVILEGES");

        Permission existingPermission = new Permission();
        existingPermission.setId(id);
        existingPermission.setName("READ_PRIVILEGES");

        Permission updatedPermission = new Permission();
        updatedPermission.setId(id);
        updatedPermission.setName("WRITE_PRIVILEGES");

        PermissionResponseDTO responseDTO = new PermissionResponseDTO();
        responseDTO.setId(id);
        responseDTO.setName("WRITE_PRIVILEGES");

        when(permissionRepository.findById(id)).thenReturn(Optional.of(existingPermission));
        doAnswer(invocation -> {
            existingPermission.setName("WRITE_PRIVILEGES");
            return null;
        }).when(permissionMapper).updatePermissionFromDto(permissionDTO, existingPermission);
        when(permissionRepository.save(existingPermission)).thenReturn(updatedPermission);
        when(permissionMapper.permissionToPermissionResponseDto(updatedPermission)).thenReturn(responseDTO);

        // Act
        PermissionResponseDTO result = permissionServiceImpl.updatePermission(id, permissionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("WRITE_PRIVILEGES", result.getName());
    }

    @Test
    void updatePermission_WhenPermissionDoesNotExist_ThrowsPermissionNotFoundException() {
        Long id = 99L;
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setName("DELETE_PRIVILEGES");

        when(permissionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PermissionNotFoundException.class, () -> permissionServiceImpl.updatePermission(id, permissionDTO));
    }

    @Test
    void deletePermission_WhenPermissionExistsAndNotAssignedToRoles_DeletesPermission() {
        Long id = 1L;
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName("READ_PRIVILEGES");

        when(permissionRepository.findById(id)).thenReturn(Optional.of(permission));
        when(rolePermissionRepository.existsByPermissionId(id)).thenReturn(false);

        permissionServiceImpl.deletePermission(id);

        verify(permissionRepository).delete(permission);
    }

    @Test
    void deletePermission_WhenPermissionDoesNotExist_ThrowsPermissionNotFoundException() {
        Long id = 99L;
        when(permissionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PermissionNotFoundException.class, () -> permissionServiceImpl.deletePermission(id));
    }

    @Test
    void deletePermission_WhenPermissionAssignedToRoles_ThrowsIllegalStateException() {
        Long id = 2L;
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName("WRITE_PRIVILEGES");

        when(permissionRepository.findById(id)).thenReturn(Optional.of(permission));
        when(rolePermissionRepository.existsByPermissionId(id)).thenReturn(true);

        Exception exception = assertThrows(IllegalStateException.class, () -> permissionServiceImpl.deletePermission(id));
        assertEquals("The permission cannot be removed because it is assigned to roles", exception.getMessage());
    }

    @Test
    void getPermissionById_WhenPermissionExists_ReturnsResponseDTO() {
        Long id = 1L;
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName("READ_PRIVILEGES");

        PermissionResponseDTO responseDTO = new PermissionResponseDTO();
        responseDTO.setId(id);
        responseDTO.setName("READ_PRIVILEGES");

        when(permissionRepository.findById(id)).thenReturn(Optional.of(permission));
        when(permissionMapper.permissionToPermissionResponseDto(permission)).thenReturn(responseDTO);

        PermissionResponseDTO result = permissionServiceImpl.getPermissionById(id);

        assertNotNull(result);
        assertEquals("READ_PRIVILEGES", result.getName());
    }

    @Test
    void getPermissionById_WhenPermissionDoesNotExist_ThrowsPermissionNotFoundException() {
        Long id = 99L;
        when(permissionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PermissionNotFoundException.class, () -> permissionServiceImpl.getPermissionById(id));
    }

    @Test
    void getAllPermissions_WhenPermissionsExist_ReturnsListOfResponseDTOs() {
        Permission p1 = new Permission(1L, "READ", new HashSet<>());
        Permission p2 = new Permission(2L, "WRITE", new HashSet<>());
        List<Permission> permissions = Arrays.asList(p1, p2);

        PermissionResponseDTO dto1 = new PermissionResponseDTO(1L, "READ");
        PermissionResponseDTO dto2 = new PermissionResponseDTO(2L, "WRITE");

        when(permissionRepository.findAll()).thenReturn(permissions);
        when(permissionMapper.permissionToPermissionResponseDto(p1)).thenReturn(dto1);
        when(permissionMapper.permissionToPermissionResponseDto(p2)).thenReturn(dto2);

        List<PermissionResponseDTO> result = permissionServiceImpl.getAllPermissions();

        assertEquals(2, result.size());
        assertEquals("READ", result.get(0).getName());
        assertEquals("WRITE", result.get(1).getName());
    }

    @Test
    void getAllPermissions_WhenNoPermissionsExist_ReturnsEmptyList() {
        when(permissionRepository.findAll()).thenReturn(Collections.emptyList());

        List<PermissionResponseDTO> result = permissionServiceImpl.getAllPermissions();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPermissionEntityById_WhenExists_ReturnsEntity() {
        Permission permission = new Permission(1L, "READ", new HashSet<>());
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));

        Permission result = permissionServiceImpl.getPermissionEntityById(1L);

        assertNotNull(result);
        assertEquals("READ", result.getName());
    }

    @Test
    void getPermissionEntityById_WhenNotExists_ThrowsException() {
        when(permissionRepository.findById(42L)).thenReturn(Optional.empty());
        assertThrows(PermissionNotFoundException.class, () -> permissionServiceImpl.getPermissionEntityById(42L));
    }
}