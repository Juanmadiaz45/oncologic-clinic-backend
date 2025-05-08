package com.oncologic.clinic.controller.rest.user;

import com.oncologic.clinic.dto.user.request.UserRequestDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.dto.user.update.UserUpdateDTO;
import com.oncologic.clinic.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsersPaginated(Pageable pageable) {
        Page<UserResponseDTO> users = userService.getAllUsersPaginated(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @RequestParam String term,
            Pageable pageable) {
        Page<UserResponseDTO> users = userService.searchUsers(term, pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserResponseDTO> addRolesToUser(
            @PathVariable Long userId,
            @RequestBody Set<Long> roleIds) {
        UserResponseDTO updatedUser = userService.addRolesToUser(userId, roleIds);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<UserResponseDTO> removeRolesFromUser(
            @PathVariable Long userId,
            @RequestBody Set<Long> roleIds) {
        UserResponseDTO updatedUser = userService.removeRolesFromUser(userId, roleIds);
        return ResponseEntity.ok(updatedUser);
    }
}
