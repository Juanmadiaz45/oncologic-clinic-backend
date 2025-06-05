package com.oncologic.clinic.service.user;

import com.oncologic.clinic.dto.registration.RegisterUserDTO;
import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.entity.user.UserRole;
import com.oncologic.clinic.exception.runtime.user.UserNotFoundException;
import com.oncologic.clinic.mapper.user.UserMapper;
import com.oncologic.clinic.repository.user.RoleRepository;
import com.oncologic.clinic.repository.user.UserRepository;
import com.oncologic.clinic.repository.user.UserRoleRepository;
import com.oncologic.clinic.service.user.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Mock
    private UserMapper userMapper;

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

        assertEquals("A user must have at least one role", exception.getMessage());
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

        assertEquals("A user must have at least one role", exception.getMessage());
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

        assertEquals("The provided roles do not exist", exception.getMessage());
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

        assertEquals("The username is already in use", exception.getMessage());
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
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setUserRoles(new HashSet<>());

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("testuser");

        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
        when(roleRepository.findAllById(userDTO.getRoleIds())).thenReturn(roles);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDTO);

        // Act
        User createdUser = userServiceImpl.createUser(userDTO);

        // Assert
        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals(1L, createdUser.getId());

        verify(userRepository, times(1)).existsByUsername(userDTO.getUsername());
        verify(roleRepository, times(1)).findAllById(userDTO.getRoleIds());
        verify(passwordEncoder, times(1)).encode(userDTO.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).userToUserResponseDto(user);
    }

    @Test
    public void updateUser_WhenRoleIdsIsNull_ShouldThrowIllegalArgumentException() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setRoleIds(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.updateUser(1L, userDTO));

        assertEquals("A user must have at least one role", exception.getMessage());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    public void updateUser_WhenRoleIdsIsEmpty_ShouldThrowIllegalArgumentException() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setRoleIds(Collections.emptySet());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.updateUser(1L, userDTO));

        assertEquals("A user must have at least one role", exception.getMessage());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    public void updateUser_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setRoleIds(Set.of(1L));

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.updateUser(1L, userDTO));

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    public void updateUser_WhenRolesDontExist_ShouldThrowIllegalArgumentException() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUsername");
        userDTO.setRoleIds(Set.of(1L, 2L));

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(roleRepository.findAllById(userDTO.getRoleIds())).thenReturn(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.updateUser(1L, userDTO));

        assertEquals("One or more provided roles do not exist", exception.getMessage());
        verify(userRoleRepository, never()).deleteByUser(any());
    }

    @Test
    public void updateUser_WhenValidUserAndRoles_ShouldUpdateUserAndRoles() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUsername");
        userDTO.setPassword("newPassword");
        userDTO.setRoleIds(Set.of(1L, 2L));

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldPassword");
        existingUser.setUserRoles(new HashSet<>());

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_USER");

        List<Role> roles = List.of(role1, role2);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("newUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("newUsername")).thenReturn(false);
        when(roleRepository.findAllById(userDTO.getRoleIds())).thenReturn(roles);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.userToUserResponseDto(existingUser)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userServiceImpl.updateUser(1L, userDTO);

        // Assert
        assertNotNull(result);
        assertEquals("newUsername", result.getUsername());

        verify(userRoleRepository).deleteByUser(existingUser);
        verify(userRepository).save(existingUser);
        verify(userMapper).updateUserFromDto(userDTO, existingUser);
        verify(userMapper).userToUserResponseDto(existingUser);
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
    public void deleteUser_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.deleteUser(userId));

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userRoleRepository, never()).deleteByUser(any());
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userId);
        userResponseDTO.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userServiceImpl.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userResponseDTO, result);
        verify(userRepository).findById(userId);
        verify(userMapper).userToUserResponseDto(user);
    }

    @Test
    public void getUserById_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.getUserById(userId));

        assertEquals("User not found with id: " + userId, exception.getMessage());
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

        UserResponseDTO userResponseDTO1 = new UserResponseDTO();
        userResponseDTO1.setId(1L);
        userResponseDTO1.setUsername("user1");

        UserResponseDTO userResponseDTO2 = new UserResponseDTO();
        userResponseDTO2.setId(2L);
        userResponseDTO2.setUsername("user2");

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.userToUserResponseDto(user1)).thenReturn(userResponseDTO1);
        when(userMapper.userToUserResponseDto(user2)).thenReturn(userResponseDTO2);

        // Act
        List<UserResponseDTO> result = userServiceImpl.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
        verify(userRepository).findAll();
        verify(userMapper, times(2)).userToUserResponseDto(any(User.class));
    }

    @Test
    public void getAllUsers_WhenNoUsersExist_ShouldReturnEmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of());

        // Act
        List<UserResponseDTO> result = userServiceImpl.getAllUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    public void addRolesToUser_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        // Arrange
        Long userId = 1L;
        Set<Long> roleIds = Set.of(1L, 2L);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.addRolesToUser(userId, roleIds));

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(roleRepository, never()).findAllById(any());
    }

    @Test
    public void addRolesToUser_WhenValidRoles_ShouldAddAllRoles() {
        // Arrange
        Long userId = 1L;
        Set<Long> roleIds = Set.of(1L, 2L);
        User user = new User();
        user.setId(userId);
        user.setUserRoles(new HashSet<>());

        Role role1 = new Role();
        role1.setId(1L);
        Role role2 = new Role();
        role2.setId(2L);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findAllById(roleIds)).thenReturn(List.of(role1, role2));
        when(userRoleRepository.existsById(any())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userServiceImpl.addRolesToUser(userId, roleIds);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository).save(user);
        verify(userMapper).userToUserResponseDto(user);
    }

    @Test
    public void removeRolesFromUser_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        // Arrange
        Long userId = 1L;
        Set<Long> roleIds = Set.of(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.removeRolesFromUser(userId, roleIds));

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userRoleRepository, never()).deleteAll(any());
    }

    @Test
    public void removeRolesFromUser_WhenRemovingAllRoles_ShouldThrowIllegalStateException() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

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

        assertEquals("A user must have at least one role", exception.getMessage());
        verify(userRoleRepository, never()).deleteAll(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void removeRolesFromUser_WhenValidRemoval_ShouldDeleteSpecifiedRoles() {
        // Arrange
        Long userId = 1L;
        Long roleIdToRemove = 1L;

        User user = getUser(userId, roleIdToRemove);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userServiceImpl.removeRolesFromUser(userId, Set.of(roleIdToRemove));

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertFalse(result.getId().equals(roleIdToRemove));
        assertEquals(2, user.getUserRoles().size());
        verify(userRepository).save(user);
        verify(userRoleRepository).deleteAll(any());
        verify(userMapper).userToUserResponseDto(user);
    }

    private static User getUser(Long userId, Long roleIdToRemove) {
        User user = new User();
        user.setId(userId);

        Role role1 = new Role();
        role1.setId(roleIdToRemove);
        Role role2 = new Role();
        role2.setId(2L);
        Role role3 = new Role();
        role3.setId(3L);

        UserRole userRole1 = new UserRole(new UserRole.UserRoleId(userId, roleIdToRemove), user, role1);
        UserRole userRole2 = new UserRole(new UserRole.UserRoleId(userId, 2L), user, role2);
        UserRole userRole3 = new UserRole(new UserRole.UserRoleId(userId, 3L), user, role3);

        user.setUserRoles(new HashSet<>(Set.of(userRole1, userRole2, userRole3)));
        return user;
    }

    @Test
    public void removeRolesFromUser_WhenMultipleRoles_ShouldDeleteAllSpecifiedRoles() {
        // Arrange
        Long userId = 1L;
        User user = getUser(userId);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userServiceImpl.removeRolesFromUser(userId, Set.of(1L, 2L));

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(2, user.getUserRoles().size());
        assertFalse(user.getUserRoles().stream().anyMatch(ur -> ur.getRole().getId().equals(1L)));
        assertFalse(user.getUserRoles().stream().anyMatch(ur -> ur.getRole().getId().equals(2L)));
        verify(userRepository).save(user);
        verify(userRoleRepository).deleteAll(any());
        verify(userMapper).userToUserResponseDto(user);
    }

    private static User getUser(Long userId) {
        User user = new User();
        user.setId(userId);

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_1");
        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_2");
        Role role3 = new Role();
        role3.setId(3L);
        role3.setName("ROLE_3");
        Role role4 = new Role();
        role4.setId(4L);
        role4.setName("ROLE_4");

        UserRole ur1 = new UserRole(new UserRole.UserRoleId(userId, 1L), user, role1);
        UserRole ur2 = new UserRole(new UserRole.UserRoleId(userId, 2L), user, role2);
        UserRole ur3 = new UserRole(new UserRole.UserRoleId(userId, 3L), user, role3);
        UserRole ur4 = new UserRole(new UserRole.UserRoleId(userId, 4L), user, role4);

        user.setUserRoles(new HashSet<>(Set.of(ur1, ur2, ur3, ur4)));
        return user;
    }
}