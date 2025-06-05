package com.oncologic.clinic.integration.user;

import com.oncologic.clinic.dto.registration.RegisterUserDTO;
import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    private Long defaultRoleId;

    @BeforeEach
    void setUp() {
        defaultRoleId = 1L;
    }

    @Test
    void createUser_WithValidDataAndRole_ShouldCreateUser() {
        // Arrange
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(defaultRoleId);
        userDTO.setRoleIds(roleIds);

        // Act
        User createdUser = userService.createUser(userDTO);

        // Assert
        assertNotNull(createdUser.getId());
        assertEquals("testuser", createdUser.getUsername());
        assertNotNull(createdUser.getPassword());
        assertFalse(createdUser.getUserRoles().isEmpty());
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(defaultRoleId);
        userDTO.setRoleIds(roleIds);
        User createdUser = userService.createUser(userDTO);

        // Act
        UserResponseDTO foundUser = userService.getUserById(createdUser.getId());

        // Assert
        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals("testuser", foundUser.getUsername());
        assertFalse(foundUser.getRoles().isEmpty());
    }

    @Test
    void updateUser_WithValidData_ShouldUpdateUser() {
        // Arrange
        RegisterUserDTO createDTO = new RegisterUserDTO();
        createDTO.setUsername("originaluser");
        createDTO.setPassword("originalpass");
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(defaultRoleId);
        createDTO.setRoleIds(roleIds);
        User createdUser = userService.createUser(createDTO);

        UserDTO updateDTO = new UserDTO();
        updateDTO.setUsername("updateduser");
        updateDTO.setPassword("updatedpass");
        updateDTO.setRoleIds(roleIds);

        // Act
        UserResponseDTO updatedUser = userService.updateUser(createdUser.getId(), updateDTO);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(createdUser.getId(), updatedUser.getId());
        assertEquals("updateduser", updatedUser.getUsername());
        assertFalse(updatedUser.getRoles().isEmpty());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldRemoveUser() {
        // Arrange
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("todelete");
        userDTO.setPassword("password");
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(defaultRoleId);
        userDTO.setRoleIds(roleIds);
        User createdUser = userService.createUser(userDTO);

        // Act
        userService.deleteUser(createdUser.getId());

        // Assert
        assertThrows(RuntimeException.class, () -> userService.getUserById(createdUser.getId()));
    }

    @Test
    void getAllUsers_WhenUsersExist_ShouldReturnUserList() {
        // Arrange
        RegisterUserDTO userDTO1 = new RegisterUserDTO();
        userDTO1.setUsername("user1");
        userDTO1.setPassword("pass1");
        Set<Long> roleIds1 = new HashSet<>();
        roleIds1.add(defaultRoleId);
        userDTO1.setRoleIds(roleIds1);
        userService.createUser(userDTO1);

        RegisterUserDTO userDTO2 = new RegisterUserDTO();
        userDTO2.setUsername("user2");
        userDTO2.setPassword("pass2");
        Set<Long> roleIds2 = new HashSet<>();
        roleIds2.add(defaultRoleId);
        userDTO2.setRoleIds(roleIds2);
        userService.createUser(userDTO2);

        // Act
        List<UserResponseDTO> users = userService.getAllUsers();

        // Assert
        assertTrue(users.size() >= 2);
    }

    @Test
    void addRolesToUser_WithValidRoleIds_ShouldAddRoles() {
        // Arrange
        Long additionalRoleId = 2L;

        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("roleuser");
        userDTO.setPassword("password");
        Set<Long> initialRoleIds = new HashSet<>();
        initialRoleIds.add(defaultRoleId);
        userDTO.setRoleIds(initialRoleIds);
        User createdUser = userService.createUser(userDTO);

        Set<Long> roleIdsToAdd = new HashSet<>();
        roleIdsToAdd.add(additionalRoleId);

        // Act
        UserResponseDTO updatedUser = userService.addRolesToUser(createdUser.getId(), roleIdsToAdd);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(2, updatedUser.getRoles().size());
    }

    @Test
    void removeRolesFromUser_WithValidRoleIds_ShouldRemoveRolesButKeepAtLeastOne() {
        // Arrange
        Long secondRoleId = 2L;
        Set<Long> initialRoleIds = new HashSet<>();
        initialRoleIds.add(defaultRoleId);
        initialRoleIds.add(secondRoleId);

        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("removeroles");
        userDTO.setPassword("password");
        userDTO.setRoleIds(initialRoleIds);
        User createdUser = userService.createUser(userDTO);

        Set<Long> rolesToRemove = new HashSet<>();
        rolesToRemove.add(secondRoleId);

        // Act
        UserResponseDTO updatedUser = userService.removeRolesFromUser(createdUser.getId(), rolesToRemove);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getRoles().size());
    }

    @Test
    void createUser_WithoutRoles_ShouldThrowException() {
        // Arrange
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("invaliduser");
        userDTO.setPassword("password");
        userDTO.setRoleIds(new HashSet<>());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void createUser_WithExistingUsername_ShouldThrowException() {
        // Arrange
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setUsername("existinguser");
        userDTO.setPassword("password");
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(defaultRoleId);
        userDTO.setRoleIds(roleIds);

        userService.createUser(userDTO);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
    }
}

