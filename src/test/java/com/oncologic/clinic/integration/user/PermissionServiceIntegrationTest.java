package com.oncologic.clinic.integration.user;

import com.oncologic.clinic.dto.user.PermissionDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.service.user.PermissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PermissionServiceIntegrationTest {

    @Autowired
    private PermissionService permissionService;

    @Test
    void getPermissionById_WhenPermissionExists_ShouldReturnPermission() {
        // Arrange

        // Act
        PermissionResponseDTO permission = permissionService.getPermissionById(1L);

        // Assert
        assertNotNull(permission);
        assertEquals("READ", permission.getName());
    }

    @Test
    void getAllPermissions_ShouldReturnAllPermissions() {
        // Act
        List<PermissionResponseDTO> permissions = permissionService.getAllPermissions();

        // Assert
        assertEquals(3, permissions.size());
        assertTrue(permissions.stream().anyMatch(p -> p.getName().equals("READ")));
        assertTrue(permissions.stream().anyMatch(p -> p.getName().equals("WRITE")));
        assertTrue(permissions.stream().anyMatch(p -> p.getName().equals("ADMIN")));
    }

    @Test
    void createPermission_WithValidData_ShouldCreateNewPermission() {
        // Arrange
        PermissionDTO newPermission = new PermissionDTO();
        newPermission.setName("DELETE");

        // Act
        PermissionResponseDTO createdPermission = permissionService.createPermission(newPermission);

        // Assert
        assertNotNull(createdPermission.getId());
        assertEquals("DELETE", createdPermission.getName());

        PermissionResponseDTO retrievedPermission = permissionService.getPermissionById(createdPermission.getId());
        assertEquals("DELETE", retrievedPermission.getName());
    }

    @Test
    void updatePermission_WithValidData_ShouldUpdatePermission() {
        // Arrange
        PermissionDTO updatedData = new PermissionDTO();
        updatedData.setName("READ_ONLY");

        // Act
        PermissionResponseDTO updatedPermission = permissionService.updatePermission(1L, updatedData);

        // Assert
        assertEquals("READ_ONLY", updatedPermission.getName());

        PermissionResponseDTO retrievedPermission = permissionService.getPermissionById(1L);
        assertEquals("READ_ONLY", retrievedPermission.getName());
    }

    @Test
    void deletePermission_WhenPermissionExists_ShouldDeletePermission() {
        // Arrange
        PermissionDTO newPermission = new PermissionDTO();
        newPermission.setName("TEMPORARY");
        PermissionResponseDTO createdPermission = permissionService.createPermission(newPermission);
        Long permissionId = createdPermission.getId();

        // Act
        permissionService.deletePermission(permissionId);

        // Assert
        Exception exception = assertThrows(RuntimeException.class, () -> permissionService.getPermissionById(permissionId));
        assertEquals("Permission not found with ID: " + permissionId, exception.getMessage());
    }

    @Test
    void createPermission_WithExistingName_ShouldThrowException() {
        // Arrange
        PermissionDTO duplicatePermission = new PermissionDTO();
        duplicatePermission.setName("READ");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> permissionService.createPermission(duplicatePermission));
    }
}
