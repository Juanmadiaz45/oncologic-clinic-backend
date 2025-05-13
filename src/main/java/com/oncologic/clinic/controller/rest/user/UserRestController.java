package com.oncologic.clinic.controller.rest.user;

import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Users", description = "Operations related to user management")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserResponseDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get paginated list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated users retrieved successfully")
    })
    @GetMapping("/paginated")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsersPaginated(Pageable pageable) {
        Page<UserResponseDTO> users = userService.getAllUsersPaginated(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Search users by a term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @RequestParam String term,
            Pageable pageable) {
        Page<UserResponseDTO> users = userService.searchUsers(term, pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Update a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add roles to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles added to user successfully"),
            @ApiResponse(responseCode = "404", description = "User or roles not found")
    })
    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserResponseDTO> addRolesToUser(
            @PathVariable Long userId,
            @RequestBody Set<Long> roleIds) {
        UserResponseDTO updatedUser = userService.addRolesToUser(userId, roleIds);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Remove roles from a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles removed from user successfully"),
            @ApiResponse(responseCode = "404", description = "User or roles not found")
    })
    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<UserResponseDTO> removeRolesFromUser(
            @PathVariable Long userId,
            @RequestBody Set<Long> roleIds) {
        UserResponseDTO updatedUser = userService.removeRolesFromUser(userId, roleIds);
        return ResponseEntity.ok(updatedUser);
    }
}
