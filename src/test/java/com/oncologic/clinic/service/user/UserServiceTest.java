package com.oncologic.clinic.service.user;

import com.oncologic.clinic.dto.registration.RegisterUserDTO;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.entity.user.UserRole;
import com.oncologic.clinic.repository.user.RoleRepository;
import com.oncologic.clinic.repository.user.UserRepository;
import com.oncologic.clinic.repository.user.UserRoleRepository;
import com.oncologic.clinic.service.user.impl.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/*
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void createUser_WhenRoleIdsIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setRoleIds(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.createUser(userDTO));

        assertEquals("Un usuario debe tener al menos un rol", exception.getMessage());
    }

    @Test
    void createUser_WhenRoleIdsIsEmpty_ShouldThrowException() {
        // Arrange
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setRoleIds(Collections.emptySet());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.createUser(userDTO));

        assertEquals("Un usuario debe tener al menos un rol", exception.getMessage());
    }

    @Test
    public void createUser_WhenRolesDontExist_ShouldThrowIllegalArgumentException() {
        // Arrange
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setRoleIds(Set.of(1L, 2L));

        when(roleRepository.findAllById(userDTO.getRoleIds())).thenReturn(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.createUser(userDTO));

        assertEquals("Los roles proporcionados no existen", exception.getMessage());
    }

    @Test
    public void createUser_WhenUsernameExists_ShouldThrowIllegalArgumentException() {
        // Arrange
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("existinguser");
        userDTO.setPassword("password");
        userDTO.setRoleIds(Set.of(1L));

        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.createUser(userDTO));

        assertEquals("El nombre de usuario ya está en uso", exception.getMessage());
    }

    @Test
    public void createUser_WhenValidUserAndRoles_ShouldCreateUserAndUserRoles() {
        // Arrange
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setRoleIds(Set.of(1L, 2L));

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_USER");

        List<Role> roles = List.of(role1, role2);

        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
        when(roleRepository.findAllById(userDTO.getRoleIds())).thenReturn(roles);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        // Act
        User createdUser = userServiceImpl.createUser(userDTO);

        // Assert
        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(1L, createdUser.getId());

        verify(userRepository, times(1)).existsByUsername(userDTO.getUsername());
        verify(roleRepository, times(1)).findAllById(userDTO.getRoleIds());
        verify(passwordEncoder, times(1)).encode(userDTO.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateUser_WhenRoleIdsIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        User user = new User();
        user.setId(1L);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.updateUser(user, null));

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

        assertEquals("Usuario no encontrado con ID: " + userId, exception.getMessage());
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

        assertEquals("Usuario no encontrado con ID: " + userId, exception.getMessage());
        verify(userRoleRepository, never()).countByUser(any());
    }

    @Test
    public void removeRolesFromUser_WhenRemovingAllRoles_ShouldThrowIllegalStateException() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        // Configurar 2 roles para el usuario
        Role role1 = new Role();
        role1.setId(1L);
        Role role2 = new Role();
        role2.setId(2L);

        UserRole userRole1 = new UserRole(new UserRole.UserRoleId(userId, 1L), user, role1);
        UserRole userRole2 = new UserRole(new UserRole.UserRoleId(userId, 2L), user, role2);

        user.setUserRoles(new HashSet<>(Set.of(userRole1, userRole2)));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> userServiceImpl.removeRolesFromUser(userId, Set.of(1L, 2L)));

        assertEquals("Un usuario debe tener al menos un rol", exception.getMessage());
        verify(userRoleRepository, never()).deleteById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void removeRolesFromUser_WhenValidRemoval_ShouldDeleteSpecifiedRoles() {
        // Arrange
        Long userId = 1L;
        Long roleIdToRemove = 1L;

        // Configurar usuario con roles
        User user = getUser(userId, roleIdToRemove);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userServiceImpl.removeRolesFromUser(userId, Set.of(roleIdToRemove));

        // Assert
        assertEquals(user, result);
        // Verificar que el rol fue eliminado de la colección
        assertFalse(result.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getId().equals(roleIdToRemove)));
        // Verificar que quedan 2 roles
        assertEquals(2, result.getUserRoles().size());
        verify(userRepository).save(user);
    }

    private static User getUser(Long userId, Long roleIdToRemove) {
        User user = new User();
        user.setId(userId);

        // Crear roles mock
        Role role1 = new Role();
        role1.setId(roleIdToRemove);
        Role role2 = new Role();
        role2.setId(2L);
        Role role3 = new Role();
        role3.setId(3L);

        // Crear UserRoles
        UserRole userRole1 = new UserRole(new UserRole.UserRoleId(userId, roleIdToRemove), user, role1);
        UserRole userRole2 = new UserRole(new UserRole.UserRoleId(userId, 2L), user, role2);
        UserRole userRole3 = new UserRole(new UserRole.UserRoleId(userId, 3L), user, role3);

        user.setUserRoles(new HashSet<>(Set.of(userRole1, userRole2, userRole3)));
        return user;
    }

    @Test
    @Transactional
    public void removeRolesFromUser_WhenMultipleRoles_ShouldDeleteAllSpecifiedRoles() {
        // Arrange
        Long userId = 1L;
        User user = getUser(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act - Eliminar 2 roles (quedarán 2)
        User result = userServiceImpl.removeRolesFromUser(userId, Set.of(1L, 2L));

        // Assert
        assertEquals(user, result);
        assertEquals(2, result.getUserRoles().size()); // Verificar que quedan 2 roles
        assertFalse(result.getUserRoles().stream().anyMatch(ur -> ur.getRole().getId().equals(1L)));
        assertFalse(result.getUserRoles().stream().anyMatch(ur -> ur.getRole().getId().equals(2L)));
        verify(userRepository).save(user);
    }

    private static User getUser(Long userId) {
        User user = new User();
        user.setId(userId);

        // Crear 4 roles (3 se mantendrán, 2 se eliminarán)
        Role role1 = new Role(1L, "ROLE_1", new HashSet<>(), new HashSet<>());
        Role role2 = new Role(2L, "ROLE_2", new HashSet<>(), new HashSet<>());
        Role role3 = new Role(3L, "ROLE_3", new HashSet<>(), new HashSet<>());
        Role role4 = new Role(4L, "ROLE_4", new HashSet<>(), new HashSet<>());

        UserRole ur1 = new UserRole(new UserRole.UserRoleId(userId, 1L), user, role1);
        UserRole ur2 = new UserRole(new UserRole.UserRoleId(userId, 2L), user, role2);
        UserRole ur3 = new UserRole(new UserRole.UserRoleId(userId, 3L), user, role3);
        UserRole ur4 = new UserRole(new UserRole.UserRoleId(userId, 4L), user, role4);

        user.setUserRoles(new HashSet<>(Set.of(ur1, ur2, ur3, ur4)));
        return user;
    }
}

 */