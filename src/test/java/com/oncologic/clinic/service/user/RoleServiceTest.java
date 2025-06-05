package com.oncologic.clinic.service.user;

import com.oncologic.clinic.dto.user.RoleDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.RolePermission;
import com.oncologic.clinic.exception.runtime.user.PermissionNotFoundException;
import com.oncologic.clinic.exception.runtime.user.RoleNotFoundException;
import com.oncologic.clinic.mapper.user.PermissionMapper;
import com.oncologic.clinic.mapper.user.RoleMapper;
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

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    @Test
    void createRole_WhenPermissionsExist_ReturnsSavedRole() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("ADMIN");
        roleDTO.setPermissionIds(Set.of(1L, 2L));

        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        role.setRolePermissions(new HashSet<>());

        Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setName("READ_PRIVILEGES");

        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setName("WRITE_PRIVILEGES");

        List<Permission> permissions = Arrays.asList(permission1, permission2);

        RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
        roleResponseDTO.setId(1L);
        roleResponseDTO.setName("ADMIN");

        when(roleMapper.roleDtoToRole(roleDTO)).thenReturn(role);
        when(permissionRepository.findAllById(roleDTO.getPermissionIds())).thenReturn(permissions);
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(roleMapper.roleToRoleResponseDto(role)).thenReturn(roleResponseDTO);

        // Act
        RoleResponseDTO result = roleServiceImpl.createRole(roleDTO);

        // Assert
        assertNotNull(result);
        assertEquals("ADMIN", result.getName());

        verify(roleMapper).roleDtoToRole(roleDTO);
        verify(permissionRepository).findAllById(roleDTO.getPermissionIds());
        verify(roleRepository, times(2)).save(role);
        verify(roleMapper).roleToRoleResponseDto(role);
    }

    @Test
    void createRole_WhenPermissionIdsIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("ADMIN");
        roleDTO.setPermissionIds(new HashSet<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> roleServiceImpl.createRole(roleDTO));

        assertEquals("A role must have at least one permission", exception.getMessage());
        verify(permissionRepository, never()).findAllById(any());
        verify(roleRepository, never()).save(any());
    }

    @Test
    void createRole_WhenNonExistentPermissionIdsProvided_ThrowsIllegalArgumentException() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("GUEST");
        roleDTO.setPermissionIds(Set.of(99L, 100L));

        when(permissionRepository.findAllById(roleDTO.getPermissionIds())).thenReturn(Collections.emptyList());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> roleServiceImpl.createRole(roleDTO));

        assertEquals("One or more provided permissions do not exist", exception.getMessage());

        verify(permissionRepository).findAllById(roleDTO.getPermissionIds());
        verify(roleRepository, never()).save(any());
    }

    @Test
    public void updateRole_WhenPermissionIdsIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setPermissionIds(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> roleServiceImpl.updateRole(1L, roleDTO));

        assertEquals("A role must have at least one permission", exception.getMessage());
        verify(roleRepository, never()).findById(anyLong());
    }

    @Test
    public void updateRole_WhenPermissionIdsIsEmpty_ShouldThrowIllegalArgumentException() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setPermissionIds(new HashSet<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> roleServiceImpl.updateRole(1L, roleDTO));

        assertEquals("A role must have at least one permission", exception.getMessage());
        verify(roleRepository, never()).findById(anyLong());
    }

    @Test
    public void updateRole_WhenRoleDoesNotExist_ShouldThrowRoleNotFoundException() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("ADMIN");
        roleDTO.setPermissionIds(Set.of(1L, 2L));

        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class,
                () -> roleServiceImpl.updateRole(1L, roleDTO));

        assertEquals("Role not found with id: 1", exception.getMessage());
        verify(roleRepository).findById(1L);
    }

    @Test
    public void updateRole_WhenPermissionsDoNotExist_ShouldThrowIllegalArgumentException() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("ADMIN");
        roleDTO.setPermissionIds(Set.of(1L, 2L));

        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setName("OLD_ADMIN");
        existingRole.setRolePermissions(new HashSet<>());

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(permissionRepository.findAllById(roleDTO.getPermissionIds())).thenReturn(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> roleServiceImpl.updateRole(1L, roleDTO));

        assertEquals("One or more provided permissions do not exist", exception.getMessage());
        verify(rolePermissionRepository, never()).deleteByRole(any());
    }

    @Test
    public void updateRole_WhenValidInput_ShouldUpdateRoleAndPermissions() {
        // Arrange
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("ADMIN");
        roleDTO.setPermissionIds(Set.of(1L, 2L));

        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setName("OLD_ADMIN");
        existingRole.setRolePermissions(new HashSet<>());

        Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setName("CREATE");

        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setName("UPDATE");

        List<Permission> permissions = Arrays.asList(permission1, permission2);

        RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
        roleResponseDTO.setId(1L);
        roleResponseDTO.setName("ADMIN");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(permissionRepository.findAllById(roleDTO.getPermissionIds())).thenReturn(permissions);
        when(roleRepository.save(existingRole)).thenReturn(existingRole);
        when(roleMapper.roleToRoleResponseDto(existingRole)).thenReturn(roleResponseDTO);

        // Act
        RoleResponseDTO result = roleServiceImpl.updateRole(1L, roleDTO);

        // Assert
        assertNotNull(result);
        assertEquals("ADMIN", result.getName());

        verify(roleMapper).updateRoleFromDto(roleDTO, existingRole);
        verify(rolePermissionRepository).deleteByRole(existingRole);
        verify(roleRepository, times(2)).save(existingRole);
        verify(roleMapper).roleToRoleResponseDto(existingRole);
    }

    @Test
    public void deleteRole_WhenRoleDoesNotExist_ShouldThrowRoleNotFoundException() {
        // Arrange
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class,
                () -> roleServiceImpl.deleteRole(roleId));

        assertEquals("Role not found with id: " + roleId, exception.getMessage());

        verify(rolePermissionRepository, never()).deleteByRole(any());
        verify(roleRepository, never()).delete(any());
    }

    @Test
    public void deleteRole_WhenRoleHasAssignedUsers_ShouldThrowIllegalStateException() {
        // Arrange
        Long roleId = 1L;
        Role role = new Role();
        role.setId(roleId);
        role.setName("ADMIN");
        role.setRolePermissions(new HashSet<>());
        role.setUserRoles(new HashSet<>());

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRoleRepository.existsByRoleId(roleId)).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> roleServiceImpl.deleteRole(roleId));

        assertEquals("The role cannot be deleted because it has users assigned to it.", exception.getMessage());
        verify(roleRepository).findById(roleId);
        verify(userRoleRepository).existsByRoleId(roleId);
        verify(rolePermissionRepository, never()).deleteByRole(any());
        verify(roleRepository, never()).delete(any());
    }

    @Test
    public void deleteRole_WhenValidRoleWithoutUsers_ShouldDeleteRoleAndPermissions() {
        // Arrange
        Long roleId = 1L;
        Role role = new Role();
        role.setId(roleId);
        role.setName("ADMIN");
        role.setRolePermissions(new HashSet<>());

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRoleRepository.existsByRoleId(roleId)).thenReturn(false);

        // Act
        roleServiceImpl.deleteRole(roleId);

        // Assert
        verify(roleRepository).findById(roleId);
        verify(userRoleRepository).existsByRoleId(roleId);
        verify(rolePermissionRepository).deleteByRole(role);
        verify(roleRepository).delete(role);
    }

    @Test
    public void getRoleById_WhenRoleExists_ShouldReturnRole() {
        // Arrange
        Long roleId = 1L;
        Role role = new Role();
        role.setId(roleId);
        role.setName("ADMIN");

        RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
        roleResponseDTO.setId(roleId);
        roleResponseDTO.setName("ADMIN");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleMapper.roleToRoleResponseDto(role)).thenReturn(roleResponseDTO);

        // Act
        RoleResponseDTO result = roleServiceImpl.getRoleById(roleId);

        // Assert
        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
        verify(roleRepository).findById(roleId);
        verify(roleMapper).roleToRoleResponseDto(role);
    }

    @Test
    public void getRoleById_WhenRoleDoesNotExist_ShouldThrowRoleNotFoundException() {
        // Arrange
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class,
                () -> roleServiceImpl.getRoleById(roleId));

        assertEquals("Role not found with id: " + roleId, exception.getMessage());
        verify(roleRepository).findById(roleId);
    }

    @Test
    public void getAllRoles_WhenRolesExist_ShouldReturnRoleList() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("USER");

        List<Role> roles = Arrays.asList(role1, role2);

        RoleResponseDTO roleResponseDTO1 = new RoleResponseDTO();
        roleResponseDTO1.setId(1L);
        roleResponseDTO1.setName("ADMIN");

        RoleResponseDTO roleResponseDTO2 = new RoleResponseDTO();
        roleResponseDTO2.setId(2L);
        roleResponseDTO2.setName("USER");

        when(roleRepository.findAll()).thenReturn(roles);
        when(roleMapper.roleToRoleResponseDto(role1)).thenReturn(roleResponseDTO1);
        when(roleMapper.roleToRoleResponseDto(role2)).thenReturn(roleResponseDTO2);

        // Act
        List<RoleResponseDTO> result = roleServiceImpl.getAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getName());
        assertEquals("USER", result.get(1).getName());
        verify(roleRepository).findAll();
        verify(roleMapper, times(2)).roleToRoleResponseDto(any(Role.class));
    }

    @Test
    public void getAllRoles_WhenNoRolesExist_ShouldReturnEmptyList() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<RoleResponseDTO> result = roleServiceImpl.getAllRoles();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roleRepository).findAll();
    }

    @Test
    void addPermissionsToRole_WhenRoleExists_ShouldAddPermissionsAndReturnUpdatedRole() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIds = Set.of(10L, 20L);

        Role role = new Role();
        role.setId(roleId);
        role.setName("ADMIN");
        role.setRolePermissions(new HashSet<>());

        Permission permission1 = new Permission();
        permission1.setId(10L);
        permission1.setName("READ");

        Permission permission2 = new Permission();
        permission2.setId(20L);
        permission2.setName("WRITE");

        List<Permission> permissions = Arrays.asList(permission1, permission2);

        RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
        roleResponseDTO.setId(roleId);
        roleResponseDTO.setName("ADMIN");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(permissionRepository.findAllById(permissionIds)).thenReturn(permissions);
        when(rolePermissionRepository.existsById(any())).thenReturn(false);
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.roleToRoleResponseDto(role)).thenReturn(roleResponseDTO);

        // Act
        RoleResponseDTO updatedRole = roleServiceImpl.addPermissionsToRole(roleId, permissionIds);

        // Assert
        assertNotNull(updatedRole);
        assertEquals("ADMIN", updatedRole.getName());
        verify(roleRepository).findById(roleId);
        verify(permissionRepository).findAllById(permissionIds);
        verify(roleRepository).save(role);
        verify(roleMapper).roleToRoleResponseDto(role);
    }

    @Test
    public void addPermissionsToRole_WhenRoleNotFound_ShouldThrowRoleNotFoundException() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIds = Set.of(1L, 2L);
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class,
                () -> roleServiceImpl.addPermissionsToRole(roleId, permissionIds));

        assertEquals("Role not found with id: " + roleId, exception.getMessage());
        verify(roleRepository).findById(roleId);
        verifyNoInteractions(permissionRepository, rolePermissionRepository);
    }

    @Test
    void addPermissionsToRole_WhenNoPermissionsAreProvided_ShouldThrowIllegalArgumentException() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIds = new HashSet<>();

        Role role = new Role();
        role.setId(roleId);
        role.setName("USER");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> roleServiceImpl.addPermissionsToRole(roleId, permissionIds));

        assertEquals("You must provide at least one permit", exception.getMessage());
        verify(roleRepository).findById(roleId);
        verifyNoInteractions(permissionRepository, rolePermissionRepository);
    }

    @Test
    void removePermissionsFromRole_WhenRoleExistsAndHasRemainingPermissions_ShouldRemovePermissions() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIds = Set.of(10L, 20L);

        Role role = getMockRole(roleId);
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
        roleResponseDTO.setId(roleId);
        roleResponseDTO.setName("ADMIN");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.roleToRoleResponseDto(role)).thenReturn(roleResponseDTO);

        // Act
        RoleResponseDTO updatedRole = roleServiceImpl.removePermissionsFromRole(roleId, permissionIds);

        // Assert
        assertNotNull(updatedRole);
        assertEquals("ADMIN", updatedRole.getName());
        verify(roleRepository).findById(roleId);
        verify(rolePermissionRepository).deleteAll(any());
        verify(roleRepository).save(role);
        verify(roleMapper).roleToRoleResponseDto(role);
    }

    @Test
    public void removePermissionsFromRole_WhenRoleDoesNotExist_ShouldThrowRoleNotFoundException() {
        // Arrange
        Long roleId = 99L;
        Set<Long> permissionIds = Set.of(1L, 2L);

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class,
                () -> roleServiceImpl.removePermissionsFromRole(roleId, permissionIds));

        assertEquals("Role not found with id: " + roleId, exception.getMessage());
        verify(roleRepository).findById(roleId);
        verifyNoInteractions(rolePermissionRepository);
    }

    @Test
    void removePermissionsFromRole_WhenRemovalWouldLeaveRoleWithoutPermissions_ShouldThrowIllegalStateException() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIds = Set.of(10L, 20L, 30L);

        Role role = getMockRole(roleId);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> roleServiceImpl.removePermissionsFromRole(roleId, permissionIds));

        assertEquals("A role must have at least one permission", exception.getMessage());
        verify(roleRepository).findById(roleId);
        verify(rolePermissionRepository, never()).deleteAll(any());
        verify(roleRepository, never()).save(any());
    }

    @Test
    public void removePermissionsFromRole_WhenPermissionToRemoveDoesNotExist_ShouldThrowPermissionNotFoundException() {
        // Arrange
        Long roleId = 1L;
        Set<Long> permissionIds = Set.of(99L);

        Role role = getMockRole(roleId);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Act & Assert
        PermissionNotFoundException exception = assertThrows(PermissionNotFoundException.class,
                () -> roleServiceImpl.removePermissionsFromRole(roleId, permissionIds));

        assertEquals("One or more permissions are not associated with the role", exception.getMessage());
        verify(roleRepository).findById(roleId);
        verify(rolePermissionRepository, never()).deleteAll(any());
    }

    @Test
    public void getPermissionsByRoleId_WhenPermissionsExist_ShouldReturnPermissionList() {
        // Arrange
        Long roleId = 1L;
        Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setName("READ");

        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setName("WRITE");

        RolePermission rp1 = new RolePermission();
        rp1.setPermission(permission1);

        RolePermission rp2 = new RolePermission();
        rp2.setPermission(permission2);

        List<RolePermission> rolePermissions = Arrays.asList(rp1, rp2);

        PermissionResponseDTO permissionResponseDTO1 = new PermissionResponseDTO();
        permissionResponseDTO1.setId(1L);
        permissionResponseDTO1.setName("READ");

        PermissionResponseDTO permissionResponseDTO2 = new PermissionResponseDTO();
        permissionResponseDTO2.setId(2L);
        permissionResponseDTO2.setName("WRITE");

        when(rolePermissionRepository.findPermissionsByRoleId(roleId)).thenReturn(rolePermissions);
        when(permissionMapper.permissionToPermissionResponseDto(permission1)).thenReturn(permissionResponseDTO1);
        when(permissionMapper.permissionToPermissionResponseDto(permission2)).thenReturn(permissionResponseDTO2);

        // Act
        List<PermissionResponseDTO> result = roleServiceImpl.getPermissionsByRoleId(roleId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("READ", result.get(0).getName());
        assertEquals("WRITE", result.get(1).getName());
        verify(rolePermissionRepository).findPermissionsByRoleId(roleId);
        verify(permissionMapper, times(2)).permissionToPermissionResponseDto(any(Permission.class));
    }

    @Test
    public void getRoleEntityById_WhenRoleExists_ShouldReturnRole() {
        // Arrange
        Long roleId = 1L;
        Role role = new Role();
        role.setId(roleId);
        role.setName("ADMIN");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Act
        Role result = roleServiceImpl.getRoleEntityById(roleId);

        // Assert
        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
        verify(roleRepository).findById(roleId);
    }

    @Test
    public void getRoleEntityById_WhenRoleDoesNotExist_ShouldThrowRoleNotFoundException() {
        // Arrange
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class,
                () -> roleServiceImpl.getRoleEntityById(roleId));

        assertEquals("Role not found with id: " + roleId, exception.getMessage());
        verify(roleRepository).findById(roleId);
    }

    @Test
    public void getAllRoleEntities_WhenRolesExist_ShouldReturnRoleList() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("USER");

        List<Role> roles = Arrays.asList(role1, role2);

        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        List<Role> result = roleServiceImpl.getAllRoleEntities();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getName());
        assertEquals("USER", result.get(1).getName());
        verify(roleRepository).findAll();
    }

    @Test
    public void getAllRoleEntities_WhenNoRolesExist_ShouldReturnEmptyList() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Role> result = roleServiceImpl.getAllRoleEntities();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roleRepository).findAll();
    }

    private static Role getMockRole(Long roleId) {
        Role role = new Role();
        role.setId(roleId);
        role.setName("ADMIN");

        Permission perm1 = new Permission();
        perm1.setId(10L);
        perm1.setName("READ");

        Permission perm2 = new Permission();
        perm2.setId(20L);
        perm2.setName("WRITE");

        Permission perm3 = new Permission();
        perm3.setId(30L);
        perm3.setName("DELETE");

        RolePermission rp1 = new RolePermission(new RolePermission.RolePermissionId(roleId, 10L), role, perm1);
        RolePermission rp2 = new RolePermission(new RolePermission.RolePermissionId(roleId, 20L), role, perm2);
        RolePermission rp3 = new RolePermission(new RolePermission.RolePermissionId(roleId, 30L), role, perm3);

        role.setRolePermissions(new HashSet<>(Arrays.asList(rp1, rp2, rp3)));
        return role;
    }
}