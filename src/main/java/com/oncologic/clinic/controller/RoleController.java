package com.oncologic.clinic.controller;

import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.service.user.PermissionService;
import com.oncologic.clinic.service.user.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    public RoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping("/create")
    public String showCreateRoleForm(Model model) {
        if (!model.containsAttribute("role")) {
            model.addAttribute("role", new Role());
        }
        model.addAttribute("permissions", permissionService.getAllPermissions());
        return "create-role";
    }

    @PostMapping("/create")
    public String createRole(@ModelAttribute("role") Role role,
                             @RequestParam(required = false) Set<Long> permissionIds,
                             RedirectAttributes redirectAttributes) {
        try {
            roleService.createRole(role, permissionIds);
            redirectAttributes.addFlashAttribute("successMessage", "Rol creado exitosamente");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("role", role);
            return "redirect:/roles/create";
        }
    }

    @GetMapping("/permissions")
    public String manageRolePermissions(
            @RequestParam(required = false) Long roleId,
            Model model) {

        model.addAttribute("allRoles", roleService.getAllRoles());

        if (roleId != null) {
            Role role = roleService.getRoleById(roleId);
            model.addAttribute("selectedRole", role);

            List<Permission> permissions = roleService.getPermissionsByRoleId(roleId);
            model.addAttribute("selectedPermissions", permissions);
        }

        model.addAttribute("allPermissions", permissionService.getAllPermissions());

        return "role-permissions";
    }

    @PostMapping("/permissions/update")
    public String updateRolePermissions(
            @RequestParam Long roleId,
            @RequestParam(required = false) Set<Long> permissionIds,
            RedirectAttributes redirectAttributes) {

        try {
            Role role = roleService.getRoleById(roleId);
            roleService.updateRole(role, permissionIds != null ? permissionIds : new HashSet<>());
            redirectAttributes.addFlashAttribute("successMessage", "Permisos actualizados correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar permisos: " + e.getMessage());
        }

        return "redirect:/roles/permissions?roleId=" + roleId;
    }

    @GetMapping("/delete")
    public String showDeleteRoleForm(@RequestParam(required = false) Long roleId, Model model) {
        model.addAttribute("allRoles", roleService.getAllRoles());
        if (roleId != null) {
            model.addAttribute("selectedRole", roleService.getRoleById(roleId));
        }
        return "delete-role";
    }

    @PostMapping("/delete")
    public String deleteRole(
            @RequestParam Long roleId,
            RedirectAttributes redirectAttributes) {

        try {
            String roleName = roleService.getRoleById(roleId).getName();
            roleService.deleteRole(roleId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Rol '" + roleName + "' eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al eliminar rol: " + e.getMessage());
        }

        return "redirect:/roles/delete";
    }
}