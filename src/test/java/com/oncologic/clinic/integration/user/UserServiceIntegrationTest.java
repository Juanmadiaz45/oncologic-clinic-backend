/*package com.oncologic.clinic.integration.user;

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
        defaultRoleId = 1L; // ID de un rol existente en tu BD de prueba
    }

    @Test
    void createUser_WithValidDataAndRole_ShouldCreateUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(defaultRoleId);

        // Act
        User createdUser = userService.createUser(user, roleIds);

        // Assert
        assertNotNull(createdUser.getId());
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("password123", createdUser.getPassword());
        assertFalse(createdUser.getUserRoles().isEmpty());
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(defaultRoleId);
        User createdUser = userService.createUser(user, roleIds);

        // Act
        User foundUser = userService.getUserById(createdUser.getId());

        // Assert
        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals("testuser", foundUser.getUsername());
        assertFalse(foundUser.getUserRoles().isEmpty());
    }

    @Test
    void updateUser_WithValidData_ShouldUpdateUser() {
        // Arrange
        User user = new User();
        user.setUsername("originaluser");
        user.setPassword("originalpass");
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(defaultRoleId);
        User createdUser = userService.createUser(user, roleIds);

        User updateData = new User();
        updateData.setId(createdUser.getId());
        updateData.setUsername("updateduser");
        updateData.setPassword("updatedpass");

        // Act
        User updatedUser = userService.updateUser(updateData, roleIds);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(createdUser.getId(), updatedUser.getId());
        assertEquals("updateduser", updatedUser.getUsername());
        assertEquals("updatedpass", updatedUser.getPassword());
        assertFalse(updatedUser.getUserRoles().isEmpty());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldRemoveUser() {
        // Arrange
        User user = new User();
        user.setUsername("todelete");
        user.setPassword("password");
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(defaultRoleId);
        User createdUser = userService.createUser(user, roleIds);

        // Act
        userService.deleteUser(createdUser.getId());

        // Assert
        assertThrows(RuntimeException.class, () -> userService.getUserById(createdUser.getId()));
    }

    @Test
    void getAllUsers_WhenUsersExist_ShouldReturnUserList() {
        // Arrange
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("pass1");
        Set<Long> roleIds1 = new HashSet<>();
        roleIds1.add(defaultRoleId);
        userService.createUser(user1, roleIds1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("pass2");
        Set<Long> roleIds2 = new HashSet<>();
        roleIds2.add(defaultRoleId);
        userService.createUser(user2, roleIds2);

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertTrue(users.size() >= 2);
    }

    @Test
    void addRolesToUser_WithValidRoleIds_ShouldAddRoles() {
        // Arrange
        // Asumimos que existe otro rol con ID 2 en la BD
        Long additionalRoleId = 2L;

        User user = new User();
        user.setUsername("roleuser");
        user.setPassword("password");
        Set<Long> initialRoleIds = new HashSet<>();
        initialRoleIds.add(defaultRoleId);
        User createdUser = userService.createUser(user, initialRoleIds);

        Set<Long> roleIdsToAdd = new HashSet<>();
        roleIdsToAdd.add(additionalRoleId);

        // Act
        User updatedUser = userService.addRolesToUser(createdUser.getId(), roleIdsToAdd);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(2, updatedUser.getUserRoles().size());
    }

    @Test
    void removeRolesFromUser_WithValidRoleIds_ShouldRemoveRolesButKeepAtLeastOne() {
        // Arrange
        // Creamos usuario con dos roles
        Long secondRoleId = 2L;
        Set<Long> initialRoleIds = new HashSet<>();
        initialRoleIds.add(defaultRoleId);
        initialRoleIds.add(secondRoleId);

        User user = new User();
        user.setUsername("removeroles");
        user.setPassword("password");
        User createdUser = userService.createUser(user, initialRoleIds);

        // Preparamos roles a remover (solo uno de los dos)
        Set<Long> rolesToRemove = new HashSet<>();
        rolesToRemove.add(secondRoleId);

        // Act
        User updatedUser = userService.removeRolesFromUser(createdUser.getId(), rolesToRemove);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getUserRoles().size());
    }

    @Test
    void createUser_WithoutRoles_ShouldThrowException() {
        // Arrange
        User user = new User();
        user.setUsername("invaliduser");
        user.setPassword("password");
        Set<Long> emptyRoleIds = new HashSet<>();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user, emptyRoleIds));
    }
}*/