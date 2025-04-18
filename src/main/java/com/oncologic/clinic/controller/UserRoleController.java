package com.oncologic.clinic.controller;

import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.service.user.RoleService;
import com.oncologic.clinic.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/users/roles")
public class UserRoleController {

    private final UserService userService;
    private final RoleService roleService;

    public UserRoleController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/assign")
    public String showAssignRoleForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        return "assign-role";
    }

    @PostMapping("/assign")
    public String assignRoleToUser(
            @RequestParam Long userId,
            @RequestParam(value = "roleIds", required = false) Set<Long> roleIds,
            RedirectAttributes redirectAttributes) {

        try {
            if (roleIds == null) {
                roleIds = new HashSet<>();
            }

            userService.addRolesToUser(userId, roleIds);
            redirectAttributes.addFlashAttribute("successMessage", "Roles asignados correctamente al usuario");
            return "redirect:/dashboard?roleAssigned";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/roles/assign";
        }
    }

    @GetMapping("/remove")
    public String showRemoveRoleForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "remove-role";
    }

    @GetMapping("/user-roles")
    @ResponseBody
    public List<Map<String, Object>> getUserRoles(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        return user.getUserRoles().stream()
                .map(userRole -> {
                    Role role = userRole.getRole();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", role.getId());
                    map.put("name", role.getName());
                    return map;
                })
                .toList();
    }

    @PostMapping("/remove")
    public String removeRoleFromUser(
            @RequestParam Long userId,
            @RequestParam Set<Long> roleIds,
            RedirectAttributes redirectAttributes) {

        try {
            userService.removeRolesFromUser(userId, roleIds);
            redirectAttributes.addFlashAttribute("successMessage", "Roles eliminados correctamente del usuario");
            return "redirect:/dashboard?roleRemoved";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/roles/remove";
        }
    }
}