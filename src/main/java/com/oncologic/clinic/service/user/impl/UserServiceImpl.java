package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.entity.user.*;
import com.oncologic.clinic.repository.user.*;
import com.oncologic.clinic.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public User createUser(User user, Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Un usuario debe tener al menos un rol");
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("Los roles proporcionados no existen");
        }

        User savedUser = userRepository.save(user);

        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setId(new UserRole.UserRoleId(savedUser.getId(), role.getId()));
            userRole.setUser(savedUser);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        return savedUser;
    }

    @Override
    public User updateUser(User user, Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Un usuario debe tener al menos un rol");
        }

        User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("Los roles proporcionados no existen");
        }

        userRoleRepository.deleteByUser(existingUser);

        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setId(new UserRole.UserRoleId(existingUser.getId(), role.getId()));
            userRole.setUser(existingUser);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        userRoleRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User addRolesToUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));

        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setId(new UserRole.UserRoleId(user.getId(), role.getId()));
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        return userRepository.save(user);
    }

    @Override
    public User removeRolesFromUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        long remainingRoles = userRoleRepository.countByUser(user) - roleIds.size();
        if (remainingRoles <= 0) {
            throw new IllegalStateException("Un usuario debe tener al menos un rol");
        }

        for (Long roleId : roleIds) {
            userRoleRepository.deleteById(new UserRole.UserRoleId(userId, roleId));
        }

        return userRepository.save(user);
    }
}
