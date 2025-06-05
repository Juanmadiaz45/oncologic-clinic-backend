package com.oncologic.clinic.integration.user;

import com.oncologic.clinic.dto.user.RoleDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
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
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("TEST_ROLE");
        roleDTO.setPermissionIds(Set.of(1L, 2L));

        // Act
        RoleResponseDTO createdRole = roleService.createRole(roleDTO);

        // Assert
        assertNotNull(createdRole.getId());
        assertEquals("TEST_ROLE", createdRole.getName());
        assertEquals(2, createdRole.getPermissions().size());
        assertTrue(createdRole.getPermissions().stream().anyMatch(p -> p.getId() == 1L));
        assertTrue(createdRole.getPermissions().stream().anyMatch(p -> p.getId() == 2L));
    }

    @Test
    void getRoleById_WithExistingId_ShouldReturnRoleWithPermissions() {
        // Arrange
        Long existingRoleId = 1L;

        // Act
        RoleResponseDTO foundRole = roleService.getRoleById(existingRoleId);

        // Assert
        assertNotNull(foundRole);
        assertEquals("USER", foundRole.getName());
        assertEquals(1, foundRole.getPermissions().size());
        assertTrue(foundRole.getPermissions().stream().anyMatch(p -> p.getId() == 1L));
    }

    @Test
    void updateRole_WithNewPermissions_ShouldUpdateRoleAndPermissions() {
        // Arrange
        Long roleId = 2L;
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("UPDATED_EDITOR");
        roleDTO.setPermissionIds(Set.of(1L, 3L));

        // Act
        RoleResponseDTO updatedRole = roleService.updateRole(roleId, roleDTO);

        // Assert
        assertEquals("UPDATED_EDITOR", updatedRole.getName());
        assertEquals(2, updatedRole.getPermissions().size());
        assertTrue(updatedRole.getPermissions().stream().anyMatch(p -> p.getId() == 1L));
        assertTrue(updatedRole.getPermissions().stream().anyMatch(p -> p.getId() == 3L));
        assertFalse(updatedRole.getPermissions().stream().anyMatch(p -> p.getId() == 2L));
    }

    @Test
    void addPermissionsToRole_WithNewPermissions_ShouldAddPermissions() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIdsToAdd = Set.of(2L);

        // Act
        RoleResponseDTO updatedRole = roleService.addPermissionsToRole(roleId, permissionIdsToAdd);

        // Assert
        assertEquals(2, updatedRole.getPermissions().size());
        assertTrue(updatedRole.getPermissions().stream().anyMatch(p -> p.getId() == 1L));
        assertTrue(updatedRole.getPermissions().stream().anyMatch(p -> p.getId() == 2L));
    }

    @Test
    void removePermissionsFromRole_WithExistingPermissions_ShouldRemovePermissions() {
        // Arrange
        Long roleId = 2L;
        Set<Long> permissionIdsToRemove = Set.of(1L);

        // Act
        RoleResponseDTO updatedRole = roleService.removePermissionsFromRole(roleId, permissionIdsToRemove);

        // Assert
        assertEquals(1, updatedRole.getPermissions().size());
        assertFalse(updatedRole.getPermissions().stream().anyMatch(p -> p.getId() == 1L));
        assertTrue(updatedRole.getPermissions().stream().anyMatch(p -> p.getId() == 2L));
    }

    @Test
    void getAllRoles_ShouldReturnAllRolesWithPermissions() {
        // Act
        var roles = roleService.getAllRoles();

        // Assert
        assertEquals(3, roles.size());
        assertTrue(roles.stream().anyMatch(r -> r.getName().equals("USER")));
        assertTrue(roles.stream().anyMatch(r -> r.getName().equals("EDITOR")));
        assertTrue(roles.stream().anyMatch(r -> r.getName().equals("ADMIN")));
    }

    @Test
    void deleteRole_WithExistingId_ShouldDeleteRole() {
        // Arrange
        Long roleId = 1L;

        // Act
        roleService.deleteRole(roleId);

        // Assert
        assertThrows(RuntimeException.class, () -> roleService.getRoleById(roleId));
    }
}