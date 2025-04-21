package com.oncologic.clinic.controller;

import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.service.user.RoleService;
import com.oncologic.clinic.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users/roles")
public class UserRoleController {

    private final UserService userService;
    private final RoleService roleService;

    public UserRoleController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/manage")
    public String showManageRolesForm(Model model) {
        List<User> users = userService.getAllUsers();
        List<Role> roles = roleService.getAllRoles();

        Map<Long, Set<Long>> userRolesMap = users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> user.getUserRoles().stream()
                                .map(ur -> ur.getRole().getId())
                                .collect(Collectors.toSet())
                ));

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("userRolesMap", userRolesMap);

        return "manage-roles";
    }

    @PostMapping("/manage")
    public String manageUserRoles(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        try {
            Map<String, String[]> parameters = request.getParameterMap();

            Map<Long, Set<Long>> userRoles = new HashMap<>();

            for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
                if (entry.getKey().startsWith("userRoles[")) {
                    String key = entry.getKey();
                    Long userId = Long.parseLong(
                            key.substring("userRoles[".length(), key.length() - 1)
                    );

                    Set<Long> roleIds = new HashSet<>();
                    String[] values = entry.getValue();

                    if (values != null) {
                        for (String value : values) {
                            if (!value.isEmpty()) {
                                roleIds.add(Long.parseLong(value));
                            }
                        }
                    }

                    userRoles.put(userId, roleIds);
                }
            }

            processRoleChanges(userRoles);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Roles actualizados correctamente"
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error al actualizar roles: " + e.getMessage()
            );
            e.printStackTrace();
        }

        return "redirect:/users/roles/manage";
    }

    private void processRoleChanges(Map<Long, Set<Long>> newUserRoles) {
        for (Map.Entry<Long, Set<Long>> entry : newUserRoles.entrySet()) {
            Long userId = entry.getKey();
            Set<Long> newRoleIds = entry.getValue();

            Set<Long> currentRoleIds = userService.getUserById(userId).getUserRoles().stream()
                    .map(ur -> ur.getRole().getId())
                    .collect(Collectors.toSet());

            Set<Long> rolesToAdd = new HashSet<>(newRoleIds);
            rolesToAdd.removeAll(currentRoleIds);

            Set<Long> rolesToRemove = new HashSet<>(currentRoleIds);
            rolesToRemove.removeAll(newRoleIds);

            if (!rolesToAdd.isEmpty()) {
                userService.addRolesToUser(userId, rolesToAdd);
            }
            if (!rolesToRemove.isEmpty()) {
                userService.removeRolesFromUser(userId, rolesToRemove);
            }
        }
    }
}