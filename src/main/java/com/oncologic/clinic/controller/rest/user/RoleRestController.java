package com.oncologic.clinic.controller.rest.user;

import com.oncologic.clinic.dto.user.RoleDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
import com.oncologic.clinic.service.user.RoleService;
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
import java.util.Set;

@Tag(name = "Roles", description = "Operations related to roles and their permissions")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleRestController {

    private final RoleService roleService;

    @Operation(summary = "Create a new role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        RoleResponseDTO createdRole = roleService.createRole(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @Operation(summary = "Get a role by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role found"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @Operation(summary = "Get all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of roles retrieved")
    })
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Operation(summary = "Update a role by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.updateRole(id, roleDTO));
    }

    @Operation(summary = "Delete a role by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get permissions assigned to a role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping("/{roleId}/permissions")
    public ResponseEntity<List<PermissionResponseDTO>> getRolePermissions(@PathVariable Long roleId) {
        return ResponseEntity.ok(roleService.getPermissionsByRoleId(roleId));
    }

    @Operation(summary = "Assign permissions to a role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions added successfully"),
            @ApiResponse(responseCode = "404", description = "Role or permissions not found")
    })
    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<RoleResponseDTO> addPermissionsToRole(
            @PathVariable Long roleId,
            @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(roleService.addPermissionsToRole(roleId, permissionIds));
    }

    @Operation(summary = "Remove permissions from a role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions removed successfully"),
            @ApiResponse(responseCode = "404", description = "Role or permissions not found")
    })
    @DeleteMapping("/{roleId}/permissions")
    public ResponseEntity<RoleResponseDTO> removePermissionsFromRole(
            @PathVariable Long roleId,
            @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(roleService.removePermissionsFromRole(roleId, permissionIds));
    }
}