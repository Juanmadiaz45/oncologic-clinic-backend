package com.oncologic.clinic.controller.rest.user;

import com.oncologic.clinic.dto.user.request.PermissionRequestDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.update.PermissionUpdateDTO;
import com.oncologic.clinic.service.user.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionRestController {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionResponseDTO> createPermission(
            @Valid @RequestBody PermissionRequestDTO permissionDTO) {
        PermissionResponseDTO createdPermission = permissionService.createPermission(permissionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponseDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionUpdateDTO permissionDTO) {
        return ResponseEntity.ok(permissionService.updatePermission(id, permissionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}