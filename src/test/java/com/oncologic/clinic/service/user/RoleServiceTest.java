/*package com.oncologic.clinic.service.user;

import com.oncologic.clinic.entity.user.*;
import com.oncologic.clinic.repository.user.PermissionRepository;
import com.oncologic.clinic.repository.user.RolePermissionRepository;
import com.oncologic.clinic.repository.user.RoleRepository;
import com.oncologic.clinic.repository.user.UserRoleRepository;
import com.oncologic.clinic.service.user.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    @Test
    void createRole_WhenPermissionsExist_ReturnsSavedRole() {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        Permission permission1 = new Permission(1L, "READ_PRIVILEGES", new HashSet<>());
        Permission permission2 = new Permission(2L, "WRITE_PRIVILEGES", new HashSet<>());

        Set<Long> permissionIds = new HashSet<>(Arrays.asList(1L, 2L));
        List<Permission> permissions = Arrays.asList(permission1, permission2);

        when(permissionRepository.findAllById(permissionIds)).thenReturn(permissions);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // Act
        Role result = roleServiceImpl.createRole(role, permissionIds);

        // Assert
        assertNotNull(result);
        assertEquals("ADMIN", result.getName());

        verify(permissionRepository, times(1)).findAllById(permissionIds);
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void createRole_WhenPermissionIdsIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        Set<Long> permissionIds = new HashSet<>();

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> roleServiceImpl.createRole(role, permissionIds));

        assertEquals("Un rol debe tener al menos un permiso", exception.getMessage());
        verify(permissionRepository, never()).findAllById(any());
        verify(roleRepository, never()).save(any());
        verify(rolePermissionRepository, never()).save(any());
    }

    @Test
    void createRole_WhenNonExistentPermissionIdsProvided_ThrowsIllegalArgumentException() {
        // Arrange
        Role inputRole = new Role();
        inputRole.setName("GUEST");

        Set<Long> nonExistentPermissionIds = Set.of(99L, 100L);

        when(permissionRepository.findAllById(nonExistentPermissionIds)).thenReturn(Collections.emptyList());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> roleServiceImpl.createRole(inputRole, nonExistentPermissionIds));

        assertEquals("Los permisos proporcionados no existen", exception.getMessage());

        verify(permissionRepository).findAllById(nonExistentPermissionIds);
        verify(roleRepository, never()).save(any());
        verify(rolePermissionRepository, never()).save(any());
    }

    @Test
    public void updateRole_WhenPermissionIdsIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        Role role = new Role(1L, "ADMIN", new HashSet<>(), new HashSet<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> roleServiceImpl.updateRole(role, null));

        assertEquals("Un rol debe tener al menos un permiso", exception.getMessage());
    }

    @Test
    public void updateRole_WhenPermissionIdsIsEmpty_ShouldThrowIllegalArgumentException() {
        // Arrange
        Role role = new Role(1L, "ADMIN", new HashSet<>(), new HashSet<>());
        Set<Long> permissionIds = new HashSet<>();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> roleServiceImpl.updateRole(role, permissionIds));

        assertEquals("Un rol debe tener al menos un permiso", exception.getMessage());
    }

    @Test
    public void updateRole_WhenRoleDoesNotExist_ShouldThrowRuntimeException() {
        // Arrange
        Role role = new Role(1L, "ADMIN", new HashSet<>(), new HashSet<>());
        Set<Long> permissionIds = Set.of(1L, 2L);

        when(roleRepository.findById(role.getId())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleServiceImpl.updateRole(role, permissionIds));

        assertEquals("Rol no encontrado", exception.getMessage());
    }

    @Test
    public void updateRole_WhenPermissionsDoNotExist_ShouldThrowIllegalArgumentException() {
        // Arrange
        Role role = new Role(1L, "ADMIN", new HashSet<>(), new HashSet<>());
        Set<Long> permissionIds = Set.of(1L, 2L);
        Role existingRole = new Role(1L, "OLD_ADMIN", new HashSet<>(), new HashSet<>());

        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(existingRole));
        when(permissionRepository.findAllById(permissionIds)).thenReturn(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> roleServiceImpl.updateRole(role, permissionIds));

        assertEquals("Los permisos proporcionados no existen", exception.getMessage());
    }

    @Test
    public void updateRole_WhenValidInput_ShouldUpdateRoleAndPermissions() {
        // Arrange
        Role role = new Role(1L, "ADMIN", new HashSet<>(), new HashSet<>());
        Set<Long> permissionIds = Set.of(1L, 2L);

        Role existingRole = new Role(1L, "OLD_ADMIN", new HashSet<>(), new HashSet<>());
        Permission permission1 = new Permission(1L, "CREATE", new HashSet<>());
        Permission permission2 = new Permission(2L, "UPDATE", new HashSet<>());
        Set<Permission> permissions = Set.of(permission1, permission2);

        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(existingRole));
        when(permissionRepository.findAllById(permissionIds)).thenReturn(new ArrayList<>(permissions));
        when(roleRepository.save(existingRole)).thenReturn(existingRole);

        // Act
        Role result = roleServiceImpl.updateRole(role, permissionIds);

        // Assert
        assertNotNull(result);
        assertEquals("ADMIN", result.getName());

        verify(rolePermissionRepository, times(1)).deleteByRole(existingRole);
    }

    @Test
    public void deleteRole_WhenRoleDoesNotExist_ShouldThrowRuntimeException() {
        // Arrange
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleServiceImpl.deleteRole(roleId));

        assertEquals("Rol no encontrado", exception.getMessage());

        verify(rolePermissionRepository, never()).deleteByRole(any());
        verify(roleRepository, never()).delete(any());
    }

    @Test
    public void deleteRole_WhenRoleHasAssignedUsers_ShouldThrowRuntimeException() {
        // Arrange
        Long roleId = 1L;

        // Crear un UserRole que represente la relación con un usuario
        User user = new User();
        user.setId(1L);

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(new Role(roleId, "ADMIN", new HashSet<>(), new HashSet<>()));

        // Crear el rol con un UserRole asignado (que indica que tiene usuarios)
        Role role = new Role(roleId, "ADMIN", new HashSet<>(), // Set vacío de RolePermission
                new HashSet<>(Set.of(userRole)) // Set con un UserRole
        );

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRoleRepository.existsByRoleId(roleId)).thenReturn(true); // Mock para simular que hay usuarios

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleServiceImpl.deleteRole(roleId));

        assertEquals("No se puede eliminar el rol porque tiene usuarios asignados", exception.getMessage());
        verify(roleRepository, times(1)).findById(roleId);
        verify(userRoleRepository, times(1)).existsByRoleId(roleId); // Verificar que se chequeó
        verifyNoMoreInteractions(rolePermissionRepository);
    }

    @Test
    public void deleteRole_WhenValidRoleWithoutUsers_ShouldDeleteRoleAndPermissions() {
        // Arrange
        Long roleId = 1L;
        Role role = new Role(roleId, "ADMIN", new HashSet<>(), new HashSet<>());
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        doNothing().when(rolePermissionRepository).deleteByRole(role);
        doNothing().when(roleRepository).delete(role);

        // Act
        roleServiceImpl.deleteRole(roleId);

        // Assert
        verify(roleRepository, times(1)).findById(roleId);
        verify(rolePermissionRepository, times(1)).deleteByRole(role);
        verify(roleRepository, times(1)).delete(role);
    }

    @Test
    public void getRoleById_WhenRoleExists_ShouldReturnRole() {
        // Arrange
        Long roleId = 1L;
        Role expectedRole = new Role(roleId, "ADMIN", new HashSet<>(), new HashSet<>());
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(expectedRole));

        // Act
        Role result = roleServiceImpl.getRoleById(roleId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedRole, result);
        verify(roleRepository, times(1)).findById(roleId);
    }

    @Test
    public void getRoleById_WhenRoleDoesNotExist_ShouldThrowRuntimeException() {
        // Arrange
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleServiceImpl.getRoleById(roleId));

        assertEquals("Rol no encontrado", exception.getMessage());
        verify(roleRepository, times(1)).findById(roleId);
    }

    @Test
    public void getAllRoles_WhenRolesExist_ShouldReturnRoleList() {
        // Arrange
        List<Role> expectedRoles = Arrays.asList(new Role(1L, "ADMIN", new HashSet<>(), new HashSet<>()), new Role(2L, "USER", new HashSet<>(), new HashSet<>()));
        when(roleRepository.findAll()).thenReturn(expectedRoles);

        // Act
        List<Role> result = roleServiceImpl.getAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedRoles, result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void getAllRoles_WhenNoRolesExist_ShouldReturnEmptyList() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Role> result = roleServiceImpl.getAllRoles();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void addPermissionsToRole_WhenRoleExists_ShouldAddPermissionsAndReturnUpdatedRole() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIds = new HashSet<>(Arrays.asList(10L, 20L));

        Role role = new Role();
        role.setId(roleId);
        role.setName("ADMIN");

        Permission permission1 = new Permission(10L, "READ", new HashSet<>());
        Permission permission2 = new Permission(20L, "WRITE", new HashSet<>());
        Set<Permission> permissions = new HashSet<>(Arrays.asList(permission1, permission2));

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(permissionRepository.findAllById(permissionIds)).thenReturn(new ArrayList<>(permissions));
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // Act
        Role updatedRole = roleServiceImpl.addPermissionsToRole(roleId, permissionIds);

        // Assert
        assertNotNull(updatedRole);
        verify(roleRepository).findById(roleId);
        verify(permissionRepository).findAllById(permissionIds);
    }

    @Test
    public void addPermissionsToRole_WhenRoleNotFound_ShouldThrowRuntimeException() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIds = Set.of(1L, 2L);
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleServiceImpl.addPermissionsToRole(roleId, permissionIds));

        assertEquals("Rol no encontrado", exception.getMessage());
        verify(roleRepository, times(1)).findById(roleId);
        verifyNoInteractions(permissionRepository, rolePermissionRepository);
    }

    @Test
    void addPermissionsToRole_WhenNoPermissionsAreProvided_ShouldNotSaveAnyRolePermission() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIds = new HashSet<>();

        Role role = new Role();
        role.setId(roleId);
        role.setName("USER");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // Act
        Role updatedRole = roleServiceImpl.addPermissionsToRole(roleId, permissionIds);

        // Assert
        assertNotNull(updatedRole);
        verify(roleRepository).findById(roleId);
        verify(permissionRepository).findAllById(permissionIds);
        verify(rolePermissionRepository, never()).save(any(RolePermission.class));
        verify(roleRepository).save(role);
    }

    @Test
    void removePermissionsFromRole_WhenRoleExistsAndHasRemainingPermissions_ShouldRemovePermissions() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIds = new HashSet<>(Arrays.asList(10L, 20L));

        Role role = getMockRole(1L);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Mockear la existencia de los permisos
        when(roleRepository.save(role)).thenReturn(role);

        // Act
        Role updatedRole = roleServiceImpl.removePermissionsFromRole(roleId, permissionIds);

        // Assert
        assertNotNull(updatedRole);
        verify(roleRepository).findById(roleId);
        verify(roleRepository).save(role);
    }

    @Test
    public void removePermissionsFromRole_WhenRoleDoesNotExist_ShouldThrowRuntimeException() {
        // Arrange
        Long roleId = 99L;
        Set<Long> permissionIds = new HashSet<>(Arrays.asList(1L, 2L));

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleServiceImpl.removePermissionsFromRole(roleId, permissionIds));

        assertEquals("Rol no encontrado", exception.getMessage());
        verify(roleRepository, times(1)).findById(roleId);
        verify(rolePermissionRepository, never()).countByRole(any());
        verify(rolePermissionRepository, never()).deleteById(any());
        verify(roleRepository, never()).save(any());
    }

    @Test
    void removePermissionsFromRole_WhenRemovalWouldLeaveRoleWithoutPermissions_ShouldThrowIllegalStateException() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIds = new HashSet<>(Arrays.asList(10L, 20L, 30L));

        Role mockRole = getMockRole(roleId);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(mockRole));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> roleServiceImpl.removePermissionsFromRole(roleId, permissionIds));

        assertEquals("Un rol debe tener al menos un permiso", exception.getMessage());

        // Verificaciones
        verify(roleRepository, times(1)).findById(roleId);
        // Ya no verificamos existsById porque la nueva implementación no lo usa
        verify(roleRepository, never()).save(any());
    }

    private static Role getMockRole(Long roleId) {
        Role mockRole = new Role();
        mockRole.setId(roleId);
        mockRole.setName("ADMIN");

        // Crear permisos y asociaciones
        Permission perm1 = new Permission();
        perm1.setId(10L);
        Permission perm2 = new Permission();
        perm2.setId(20L);
        Permission perm3 = new Permission();
        perm3.setId(30L);

        RolePermission rp1 = new RolePermission();
        rp1.setRole(mockRole);
        rp1.setPermission(perm1);

        RolePermission rp2 = new RolePermission();
        rp2.setRole(mockRole);
        rp2.setPermission(perm2);

        RolePermission rp3 = new RolePermission();
        rp3.setRole(mockRole);
        rp3.setPermission(perm3);

        mockRole.setRolePermissions(new HashSet<>(Arrays.asList(rp1, rp2, rp3)));
        return mockRole;
    }

    @Test
    public void removePermissionsFromRole_WhenPermissionToRemoveDoesNotExist_ShouldThrowException() {
        // Arrange
        Long roleId = 1L;
        Long nonExistentPermissionId = 99L;
        Set<Long> permissionIds = Set.of(nonExistentPermissionId);

        Role mockRole = getMockRole(roleId);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(mockRole));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> roleServiceImpl.removePermissionsFromRole(roleId, permissionIds));

        assertEquals("Uno o más permisos no están asociados al rol", exception.getMessage());
        verify(roleRepository).findById(roleId);
        verify(rolePermissionRepository, never()).deleteById(any());
    }
}*/