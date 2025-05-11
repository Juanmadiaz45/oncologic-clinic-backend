/*package com.oncologic.clinic.integration.user;

import com.oncologic.clinic.entity.user.Permission;
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
        // Arrange - Los datos iniciales ya están en la base de datos de prueba

        // Act
        Permission permission = permissionService.getPermissionById(1L);

        // Assert
        assertNotNull(permission);
        assertEquals("READ", permission.getName());
    }

    @Test
    void getAllPermissions_ShouldReturnAllPermissions() {
        // Act
        List<Permission> permissions = permissionService.getAllPermissions();

        // Assert
        assertEquals(3, permissions.size());
        assertTrue(permissions.stream().anyMatch(p -> p.getName().equals("READ")));
        assertTrue(permissions.stream().anyMatch(p -> p.getName().equals("WRITE")));
        assertTrue(permissions.stream().anyMatch(p -> p.getName().equals("ADMIN")));
    }

    @Test
    void createPermission_WithValidData_ShouldCreateNewPermission() {
        // Arrange
        Permission newPermission = new Permission();
        newPermission.setName("DELETE");

        // Act
        Permission createdPermission = permissionService.createPermission(newPermission);

        // Assert
        assertNotNull(createdPermission.getId());
        assertEquals("DELETE", createdPermission.getName());

        // Verificar que se persistió
        Permission retrievedPermission = permissionService.getPermissionById(createdPermission.getId());
        assertEquals("DELETE", retrievedPermission.getName());
    }

    @Test
    void updatePermission_WithValidData_ShouldUpdatePermission() {
        // Arrange
        Permission permissionToUpdate = permissionService.getPermissionById(1L);
        permissionToUpdate.setName("READ_ONLY");

        // Act
        Permission updatedPermission = permissionService.updatePermission(permissionToUpdate);

        // Assert
        assertEquals("READ_ONLY", updatedPermission.getName());

        // Verificar que se actualizó en la base de datos
        Permission retrievedPermission = permissionService.getPermissionById(1L);
        assertEquals("READ_ONLY", retrievedPermission.getName());
    }

    @Test
    void deletePermission_WhenPermissionExists_ShouldDeletePermission() {
        // Crear un nuevo permiso sin relaciones
        Permission newPermission = new Permission();
        newPermission.setName("TEMPORARY");
        Permission createdPermission = permissionService.createPermission(newPermission);
        Long permissionId = createdPermission.getId();

        // Act
        permissionService.deletePermission(createdPermission.getId());
        // Assert - Verificar que al buscar el permiso eliminado se lanza la excepción esperada
        Exception exception = assertThrows(RuntimeException.class, () -> permissionService.getPermissionById(permissionId));

        assertEquals("Permiso no encontrado", exception.getMessage());

    }

    @Test
    void createPermission_WithExistingName_ShouldThrowException() {
        // Arrange
        Permission duplicatePermission = new Permission();
        duplicatePermission.setName("READ"); // Este nombre ya existe en los datos iniciales

        // Act & Assert
        assertThrows(RuntimeException.class, () -> permissionService.createPermission(duplicatePermission));
    }
}*/
