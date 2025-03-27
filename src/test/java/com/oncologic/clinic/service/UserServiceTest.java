package com.oncologic.clinic.service;

import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.entity.user.UserRole;
import com.oncologic.clinic.repository.user.RoleRepository;
import com.oncologic.clinic.repository.user.UserRepository;
import com.oncologic.clinic.repository.user.UserRoleRepository;
import com.oncologic.clinic.service.user.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void createUser_WhenRoleIdsIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        User user = new User();
        Set<Long> roleIds = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.createUser(user, roleIds));

        assertEquals("Un usuario debe tener al menos un rol", exception.getMessage());
    }

    @Test
    void createUser_WhenRoleIdsIsEmpty_ShouldThrowException() {
        // Arrange
        User user = new User();
        Set<Long> emptyRoles = Set.of();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.createUser(user, emptyRoles));

        assertEquals("Un usuario debe tener al menos un rol", exception.getMessage());
    }

    @Test
    public void createUser_WhenRolesDontExist_ShouldThrowIllegalArgumentException() {
        // Arrange
        User user = new User();
        Set<Long> roleIds = Set.of(1L, 2L);

        when(roleRepository.findAllById(roleIds)).thenReturn(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.createUser(user, roleIds));

        assertEquals("Los roles proporcionados no existen", exception.getMessage());
    }

    @Test
    public void createUser_WhenValidUserAndRoles_ShouldCreateUserAndUserRoles() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        Set<Long> roleIds = Set.of(1L, 2L);

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_USER");

        List<Role> roles = List.of(role1, role2);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("password");

        when(roleRepository.findAllById(roleIds)).thenReturn(roles);
        when(userRepository.save(user)).thenReturn(savedUser);

        // Act
        User result = userServiceImpl.createUser(user, roleIds);

        // Assert
        assertNotNull(result);
        assertEquals(savedUser, result);

        verify(userRepository, times(1)).save(user);
        verify(userRoleRepository, times(2)).save(any(UserRole.class));
    }

    @Test
    public void updateUser_WhenRoleIdsIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        User user = new User();
        user.setId(1L);
        Set<Long> roleIds = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.updateUser(user, roleIds));

        assertEquals("Un usuario debe tener al menos un rol", exception.getMessage());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    public void updateUser_WhenRoleIdsIsEmpty_ShouldThrowIllegalArgumentException() {
        // Arrange
        User user = new User();
        user.setId(1L);
        Set<Long> roleIds = new HashSet<>();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.updateUser(user, roleIds));

        assertEquals("Un usuario debe tener al menos un rol", exception.getMessage());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    public void updateUser_WhenUserNotFound_ShouldThrowRuntimeException() {
        // Arrange
        User user = new User();
        user.setId(1L);
        Set<Long> roleIds = Set.of(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImpl.updateUser(user, roleIds));

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    public void updateUser_WhenRolesDontExist_ShouldThrowIllegalArgumentException() {
        // Arrange
        User user = new User();
        user.setId(1L);
        Set<Long> roleIds = Set.of(1L, 2L);

        User existingUser = new User();
        existingUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(roleRepository.findAllById(roleIds)).thenReturn(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.updateUser(user, roleIds));

        assertEquals("Los roles proporcionados no existen", exception.getMessage());
        verify(userRoleRepository, never()).deleteByUser(any());
    }

    @Test
    public void updateUser_WhenValidUserAndRoles_ShouldUpdateUserAndRoles() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("newUsername");
        user.setPassword("newPassword");

        Set<Long> roleIds = Set.of(1L, 2L);

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldPassword");

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_USER");

        List<Role> roles = List.of(role1, role2);
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("newUsername");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(roleRepository.findAllById(roleIds)).thenReturn(roles);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        // Act
        User result = userServiceImpl.updateUser(user, roleIds);

        // Assert
        assertNotNull(result);
        assertEquals("newUsername", result.getUsername());
        assertEquals("newPassword", result.getPassword());

        verify(userRoleRepository).deleteByUser(existingUser);
        verify(userRoleRepository, times(2)).save(any(UserRole.class));
        verify(userRepository).save(existingUser);
    }

    @Test
    public void deleteUser_WhenUserExists_ShouldDeleteUserAndUserRoles() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userServiceImpl.deleteUser(userId);

        // Assert
        verify(userRoleRepository).deleteByUser(user);
        verify(userRepository).delete(user);
    }

    @Test
    public void deleteUser_WhenUserNotFound_ShouldThrowRuntimeException() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImpl.deleteUser(userId));

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRoleRepository, never()).deleteByUser(any());
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userServiceImpl.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(userRepository).findById(userId);
    }

    @Test
    public void getUserById_WhenUserNotFound_ShouldThrowRuntimeException() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImpl.getUserById(userId));

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    public void getAllUsers_WhenUsersExist_ShouldReturnUserList() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = userServiceImpl.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedUsers, result);
        verify(userRepository).findAll();
    }

    @Test
    public void getAllUsers_WhenNoUsersExist_ShouldReturnEmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of());

        // Act
        List<User> result = userServiceImpl.getAllUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    public void addRolesToUser_WhenUserNotFound_ShouldThrowRuntimeException() {
        // Arrange
        Long userId = 1L;
        Set<Long> roleIds = Set.of(1L, 2L);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImpl.addRolesToUser(userId, roleIds));

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(roleRepository, never()).findAllById(any());
    }

    @Test
    public void addRolesToUser_WhenValidRoles_ShouldAddAllRoles() {
        // Arrange
        Long userId = 1L;
        Set<Long> roleIds = Set.of(1L, 2L);
        User user = new User();
        user.setId(userId);

        Role role1 = new Role();
        role1.setId(1L);
        Role role2 = new Role();
        role2.setId(2L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findAllById(roleIds)).thenReturn(List.of(role1, role2));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userServiceImpl.addRolesToUser(userId, roleIds);

        // Assert
        assertEquals(user, result);
        verify(userRoleRepository, times(2)).save(any(UserRole.class));
        verify(userRepository).save(user);
    }

    @Test
    public void removeRolesFromUser_WhenUserNotFound_ShouldThrowRuntimeException() {
        // Arrange
        Long userId = 1L;
        Set<Long> roleIds = Set.of(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImpl.removeRolesFromUser(userId, roleIds));

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRoleRepository, never()).countByUser(any());
    }

    @Test
    public void removeRolesFromUser_WhenRemovingAllRoles_ShouldThrowIllegalStateException() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Set<Long> roleIds = Set.of(1L, 2L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRoleRepository.countByUser(user)).thenReturn(2L);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> userServiceImpl.removeRolesFromUser(userId, roleIds));

        assertEquals("Un usuario debe tener al menos un rol", exception.getMessage());
        verify(userRoleRepository, never()).deleteById(any());
    }

    @Test
    public void removeRolesFromUser_WhenValidRemoval_ShouldDeleteSpecifiedRoles() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Set<Long> roleIds = Set.of(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRoleRepository.countByUser(user)).thenReturn(3L);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userServiceImpl.removeRolesFromUser(userId, roleIds);

        // Assert
        assertEquals(user, result);
        verify(userRoleRepository).deleteById(new UserRole.UserRoleId(userId, 1L));
        verify(userRepository).save(user);
    }

    @Test
    public void removeRolesFromUser_WhenMultipleRoles_ShouldDeleteAllSpecifiedRoles() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Set<Long> roleIds = Set.of(1L, 2L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRoleRepository.countByUser(user)).thenReturn(4L);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userServiceImpl.removeRolesFromUser(userId, roleIds);

        // Assert
        assertEquals(user, result);
        verify(userRoleRepository).deleteById(new UserRole.UserRoleId(userId, 1L));
        verify(userRoleRepository).deleteById(new UserRole.UserRoleId(userId, 2L));
        verify(userRepository).save(user);
    }
}
