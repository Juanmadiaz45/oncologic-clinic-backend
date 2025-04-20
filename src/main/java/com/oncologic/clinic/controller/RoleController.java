package com.oncologic.clinic.controller;

import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.service.user.PermissionService;
import com.oncologic.clinic.service.user.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String manageRolePermissions(Model model) {
        // Implementar lógica para gestionar permisos de roles
        return "role-permissions";
    }

    @PostMapping("/delete")
    public String deleteRole(@RequestParam Long roleId,
                             RedirectAttributes redirectAttributes) {
        try {
            roleService.deleteRole(roleId);
            redirectAttributes.addFlashAttribute("successMessage", "Rol eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }
}