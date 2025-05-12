package com.oncologic.clinic.controller.rest.user;

import com.oncologic.clinic.dto.user.PermissionDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.service.user.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Permissions", description = "Operations related to user permissions")
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionRestController {

    private final PermissionService permissionService;

    @Operation(summary = "Create a new permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permission created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<PermissionResponseDTO> createPermission(
            @Valid @RequestBody PermissionDTO permissionDTO) {
        PermissionResponseDTO createdPermission = permissionService.createPermission(permissionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }

    @Operation(summary = "Get a permission by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permission found"),
            @ApiResponse(responseCode = "404", description = "Permission not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @Operation(summary = "Get all permissions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of permissions retrieved")
    })
    @GetMapping
    public ResponseEntity<List<PermissionResponseDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @Operation(summary = "Update a permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permission updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Permission not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionDTO permissionDTO) {
        return ResponseEntity.ok(permissionService.updatePermission(id, permissionDTO));
    }

    @Operation(summary = "Delete a permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permission deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Permission not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}