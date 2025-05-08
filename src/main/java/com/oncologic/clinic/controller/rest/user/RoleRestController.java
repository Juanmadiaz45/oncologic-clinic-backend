package com.oncologic.clinic.controller.rest.user;

import com.oncologic.clinic.dto.user.request.RoleRequestDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
import com.oncologic.clinic.dto.user.update.RoleUpdateDTO;
import com.oncologic.clinic.service.user.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleRestController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody RoleRequestDTO roleDTO) {
        RoleResponseDTO createdRole = roleService.createRole(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateDTO roleDTO) {
        return ResponseEntity.ok(roleService.updateRole(id, roleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{roleId}/permissions")
    public ResponseEntity<List<PermissionResponseDTO>> getRolePermissions(@PathVariable Long roleId) {
        return ResponseEntity.ok(roleService.getPermissionsByRoleId(roleId));
    }

    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<RoleResponseDTO> addPermissionsToRole(
            @PathVariable Long roleId,
            @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(roleService.addPermissionsToRole(roleId, permissionIds));
    }

    @DeleteMapping("/{roleId}/permissions")
    public ResponseEntity<RoleResponseDTO> removePermissionsFromRole(
            @PathVariable Long roleId,
            @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(roleService.removePermissionsFromRole(roleId, permissionIds));
    }
}