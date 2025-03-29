package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.entity.user.*;
import com.oncologic.clinic.entity.user.UserRole.UserRoleId;
import com.oncologic.clinic.repository.user.*;
import com.oncologic.clinic.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
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

        addRolesToUser(savedUser, roles);

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

        addRolesToUser(existingUser, roles);

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

        addRolesToUser(user, roles);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User removeRolesFromUser(Long userId, Set<Long> roleIds) {
        // Cargar el usuario con sus roles
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Validar que no se eliminen todos los roles
        long currentRoleCount = user.getUserRoles().size();
        if (currentRoleCount - roleIds.size() <= 0) {
            throw new IllegalStateException("Un usuario debe tener al menos un rol");
        }

        // Crear copia para evitar ConcurrentModificationException
        Set<UserRole> rolesToRemove = new HashSet<>();

        // Identificar roles a eliminar
        user.getUserRoles().forEach(userRole -> {
            if (roleIds.contains(userRole.getRole().getId())) {
                rolesToRemove.add(userRole);
            }
        });

        // Eliminar los roles de la colección (orphanRemoval se encargará de la eliminación física)
        rolesToRemove.forEach(userRole -> {
            user.getUserRoles().remove(userRole);
            userRole.setUser(null); // Romper relación bidireccional
        });

        return userRepository.save(user);
    }

    private void addRolesToUser(User savedUser, Set<Role> roles) {
        for (Role role : roles) {
            UserRoleId id = new UserRoleId(savedUser.getId(), role.getId());
            if (!userRoleRepository.existsById(id)) {
                UserRole userRole = new UserRole(id, savedUser, role);
                savedUser.getUserRoles().add(userRole);
            }
        }
    }
}
