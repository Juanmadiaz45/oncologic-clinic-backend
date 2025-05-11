/*package com.oncologic.clinic.service.user;

import com.oncologic.clinic.entity.user.Permission;
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

    @InjectMocks
    private PermissionServiceImpl permissionServiceImpl;


    @Test
    void createPermission_WhenValidPermission_ReturnsSavedPermission() {
        // Arrange
        Permission permission = new Permission();
        permission.setName("READ_PRIVILEGES");

        Permission savedPermission = new Permission();
        savedPermission.setId(1L);
        savedPermission.setName("READ_PRIVILEGES");

        when(permissionRepository.save(permission)).thenReturn(savedPermission);

        // Act
        Permission result = permissionServiceImpl.createPermission(permission);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("READ_PRIVILEGES", result.getName());

        verify(permissionRepository, times(1)).save(permission);
    }
    @Test
    void createPermission_WhenNullPermissionProvided_ThrowsIllegalArgumentException() {
        // Arrange - No necesario

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> permissionServiceImpl.createPermission(null));

        // Verifica que NUNCA se llamó al repositorio
        verify(permissionRepository, never()).save(any());
    }
    @Test
    void updatePermission_WhenPermissionExists_ReturnsUpdatedPermission() {
        // Arrange
        Permission existingPermission = new Permission(1L, "READ_PRIVILEGES", new HashSet<>());
        Permission updatedPermission = new Permission(1L, "WRITE_PRIVILEGES", new HashSet<>());

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(existingPermission));
        when(permissionRepository.save(any(Permission.class))).thenReturn(updatedPermission);

        // Act
        Permission result = permissionServiceImpl.updatePermission(updatedPermission);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("WRITE_PRIVILEGES", result.getName());

        verify(permissionRepository, times(1)).findById(1L);
        verify(permissionRepository, times(1)).save(existingPermission);
    }

    @Test
    void updatePermission_WhenPermissionDoesNotExist_ThrowsException() {
        // Arrange
        Permission nonExistentPermission = new Permission(99L, "DELETE_PRIVILEGES", new HashSet<>());

        when(permissionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> permissionServiceImpl.updatePermission(nonExistentPermission));

        assertEquals("Permiso no encontrado", exception.getMessage());

        verify(permissionRepository, times(1)).findById(99L);
        verify(permissionRepository, never()).save(any(Permission.class));
    }


    @Test
    void deletePermission_WhenPermissionExistsAndNotAssignedToRoles_DeletesPermission() {
        // Arrange
        Permission permission = new Permission(1L, "READ_PRIVILEGES", new HashSet<>());

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));

        // Act
        permissionServiceImpl.deletePermission(1L);

        // Assert
        verify(permissionRepository, times(1)).findById(1L);
        verify(permissionRepository, times(1)).delete(permission);
    }

    @Test
    void deletePermission_WhenPermissionDoesNotExist_ThrowsException() {
        // Arrange
        when(permissionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> permissionServiceImpl.deletePermission(99L));

        assertEquals("Permiso no encontrado", exception.getMessage());

        verify(permissionRepository, times(1)).findById(99L);
        verify(permissionRepository, never()).delete(any(Permission.class));
    }

    @Test
    void deletePermission_WhenPermissionIsAssignedToRoles_ThrowsException() {
        // Arrange
        Permission permission = new Permission();
        permission.setId(2L);
        permission.setName("WRITE_PRIVILEGES");

        // Mockea que el permiso está asignado a al menos un rol
        when(permissionRepository.findById(2L)).thenReturn(Optional.of(permission));
        when(rolePermissionRepository.existsByPermissionId(2L)).thenReturn(true); // <- Clave para el test

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> permissionServiceImpl.deletePermission(2L));

        assertEquals("No se puede eliminar el permiso porque está asignado a roles", exception.getMessage());

        verify(permissionRepository, times(1)).findById(2L);
        verify(rolePermissionRepository, times(1)).existsByPermissionId(2L); // Verifica esta interacción
        verify(permissionRepository, never()).delete(any(Permission.class));
    }

    @Test
    void getPermissionById_WhenPermissionExists_ReturnsPermission() {
        // Arrange
        Permission permission = new Permission(1L, "READ_PRIVILEGES", new HashSet<>());

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));

        // Act
        Permission result = permissionServiceImpl.getPermissionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("READ_PRIVILEGES", result.getName());
        verify(permissionRepository, times(1)).findById(1L);
    }



    @Test
    void getPermissionById_WhenPermissionDoesNotExist_ThrowsException() {
        // Arrange
        when(permissionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> permissionServiceImpl.getPermissionById(99L));

        assertEquals("Permiso no encontrado", exception.getMessage());

        verify(permissionRepository, times(1)).findById(99L);
    }

    @Test
    void getAllPermissions_WhenPermissionsExist_ReturnsPermissionList() {
        // Arrange
        Permission permission1 = new Permission(1L, "READ_PRIVILEGES", new HashSet<>());
        Permission permission2 = new Permission(2L, "WRITE_PRIVILEGES", new HashSet<>());

        List<Permission> mockPermissions = Arrays.asList(permission1, permission2);

        when(permissionRepository.findAll()).thenReturn(mockPermissions);

        // Act
        List<Permission> result = permissionServiceImpl.getAllPermissions();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("READ_PRIVILEGES", result.get(0).getName());
        assertEquals("WRITE_PRIVILEGES", result.get(1).getName());

        verify(permissionRepository, times(1)).findAll();
    }

    @Test
    void getAllPermissions_WhenNoPermissionsExist_ReturnsEmptyList() {
        // Arrange
        when(permissionRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Permission> result = permissionServiceImpl.getAllPermissions();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(permissionRepository, times(1)).findAll();
    }
}*/