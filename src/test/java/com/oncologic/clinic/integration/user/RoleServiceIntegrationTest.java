package com.oncologic.clinic.integration.user;

import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.service.user.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RoleServiceIntegrationTest {

    @Autowired
    private RoleService roleService;

    @Test
    void createRole_WithValidPermissions_ShouldCreateRoleWithPermissions() {
        // Arrange
        Role newRole = new Role();
        newRole.setName("TEST_ROLE");
        Set<Long> permissionIds = Set.of(1L, 2L); // READ y WRITE

        // Act
        Role createdRole = roleService.createRole(newRole, permissionIds);

        // Assert
        assertNotNull(createdRole.getId());
        assertEquals("TEST_ROLE", createdRole.getName());
        assertEquals(2, createdRole.getRolePermissions().size());
        assertTrue(createdRole.getRolePermissions().stream()
                .anyMatch(rp -> rp.getPermission().getId() == 1L));
        assertTrue(createdRole.getRolePermissions().stream()
                .anyMatch(rp -> rp.getPermission().getId() == 2L));
    }

    @Test
    void getRoleById_WithExistingId_ShouldReturnRoleWithPermissions() {
        // Arrange - Usando datos iniciales
        Long existingRoleId = 1L; // USER role

        // Act
        Role foundRole = roleService.getRoleById(existingRoleId);

        // Assert
        assertNotNull(foundRole);
        assertEquals("USER", foundRole.getName());
        assertEquals(1, foundRole.getRolePermissions().size());
        assertTrue(foundRole.getRolePermissions().stream()
                .anyMatch(rp -> rp.getPermission().getId() == 1L)); // READ permission
    }

    @Test
    void updateRole_WithNewPermissions_ShouldUpdateRoleAndPermissions() {
        // Arrange
        Long roleId = 2L; // EDITOR role
        Role roleToUpdate = roleService.getRoleById(roleId);
        roleToUpdate.setName("UPDATED_EDITOR");
        Set<Long> newPermissionIds = Set.of(1L, 3L); // Cambiamos de READ+WRITE a READ+ADMIN

        // Act
        Role updatedRole = roleService.updateRole(roleToUpdate, newPermissionIds);

        // Assert
        assertEquals("UPDATED_EDITOR", updatedRole.getName());
        assertEquals(2, updatedRole.getRolePermissions().size());
        assertTrue(updatedRole.getRolePermissions().stream()
                .anyMatch(rp -> rp.getPermission().getId() == 1L)); // READ
        assertTrue(updatedRole.getRolePermissions().stream()
                .anyMatch(rp -> rp.getPermission().getId() == 3L)); // ADMIN
        assertFalse(updatedRole.getRolePermissions().stream()
                .anyMatch(rp -> rp.getPermission().getId() == 2L)); // WRITE ya no debería estar
    }

    @Test
    void addPermissionsToRole_WithNewPermissions_ShouldAddPermissions() {
        // Arrange
        Long roleId = 1L; // USER role (actualmente solo tiene READ)
        Set<Long> permissionIdsToAdd = Set.of(2L); // WRITE

        // Act
        Role updatedRole = roleService.addPermissionsToRole(roleId, permissionIdsToAdd);

        // Assert
        assertEquals(2, updatedRole.getRolePermissions().size());
        assertTrue(updatedRole.getRolePermissions().stream()
                .anyMatch(rp -> rp.getPermission().getId() == 1L)); // READ original
        assertTrue(updatedRole.getRolePermissions().stream()
                .anyMatch(rp -> rp.getPermission().getId() == 2L)); // WRITE añadido
    }

    @Test
    void removePermissionsFromRole_WithExistingPermissions_ShouldRemovePermissions() {
        // Arrange
        Long roleId = 2L; // EDITOR role (tiene READ y WRITE)
        Set<Long> permissionIdsToRemove = Set.of(1L); // READ

        // Act
        Role updatedRole = roleService.removePermissionsFromRole(roleId, permissionIdsToRemove);

        // Assert
        assertEquals(1, updatedRole.getRolePermissions().size());
        assertFalse(updatedRole.getRolePermissions().stream()
                .anyMatch(rp -> rp.getPermission().getId() == 1L)); // READ eliminado
        assertTrue(updatedRole.getRolePermissions().stream()
                .anyMatch(rp -> rp.getPermission().getId() == 2L)); // WRITE permanece
    }

    @Test
    void getAllRoles_ShouldReturnAllRolesWithPermissions() {
        // Act
        var roles = roleService.getAllRoles();

        // Assert
        assertEquals(3, roles.size()); // USER, EDITOR, ADMIN
        assertTrue(roles.stream().anyMatch(r -> r.getName().equals("USER")));
        assertTrue(roles.stream().anyMatch(r -> r.getName().equals("EDITOR")));
        assertTrue(roles.stream().anyMatch(r -> r.getName().equals("ADMIN")));

        // Verificar que cada rol tiene sus permisos
        roles.forEach(role -> {
            switch (role.getName()) {
                case "USER", "ADMIN" -> assertEquals(1, role.getRolePermissions().size());
                case "EDITOR" -> assertEquals(2, role.getRolePermissions().size());
            }
        });
    }

    @Test
    void deleteRole_WithExistingId_ShouldDeleteRole() {
        // Arrange
        Long roleId = 1L; // USER role

        // Act
        roleService.deleteRole(roleId);

        // Assert
        assertThrows(RuntimeException.class, () -> roleService.getRoleById(roleId));
    }
}