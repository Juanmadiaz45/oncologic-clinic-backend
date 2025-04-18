package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.dto.info.UserInfoDTO;
import com.oncologic.clinic.dto.info.UserListGroupedDTO;
import com.oncologic.clinic.dto.registration.RegisterUserDTO;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Personal;
import com.oncologic.clinic.entity.user.*;
import com.oncologic.clinic.entity.user.UserRole.UserRoleId;
import com.oncologic.clinic.repository.user.*;
import com.oncologic.clinic.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public User registerUser(RegisterUserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        if (userDTO.getRoleIds() == null || userDTO.getRoleIds().isEmpty()) {
            throw new IllegalArgumentException("Un usuario debe tener al menos un rol");
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(userDTO.getRoleIds()));
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("Los roles proporcionados no existen");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

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
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private List<String> extractRoles(User user) {
        return user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getName())
                .toList();
    }

    @Override
    public UserListGroupedDTO listUsersGroupedByType() {
        List<User> allUsers = userRepository.findAll();

        List<UserInfoDTO> doctors = new ArrayList<>();
        List<UserInfoDTO> administratives = new ArrayList<>();
        List<UserInfoDTO> patients = new ArrayList<>();

        for (User user : allUsers) {
            List<String> roles = extractRoles(user);

            if (user.getPatient() != null){
                Patient patient = user.getPatient();
                patients.add(new UserInfoDTO(
                        user.getUsername(),
                        patient.getName(),
                        patient.getPhoneNumber(),
                        patient.getEmail(),
                        roles
                ));
            } else if (user.getPersonal() != null) {
                Personal personal = user.getPersonal();
                if (personal instanceof Doctor doctor) {
                    doctors.add(new UserInfoDTO(
                            user.getUsername(),
                            doctor.getName() + " " + doctor.getLastName(),
                            doctor.getPhoneNumber(),
                            doctor.getEmail(),
                            roles
                    ));
                } else if (personal instanceof Administrative administrative) {
                    administratives.add(new UserInfoDTO(
                            user.getUsername(),
                            administrative.getName() + " " + administrative.getLastName(),
                            administrative.getPhoneNumber(),
                            administrative.getEmail(),
                            roles
                    ));
                }
            }
        }
        return new UserListGroupedDTO(doctors, administratives, patients);
    }

    @Override
    @Transactional
    public User addRolesToUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));

        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos un rol");
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));

        if (roles.size() != roleIds.size()) {
            Set<Long> foundRoleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
            Set<Long> missingRoleIds = roleIds.stream()
                    .filter(id -> !foundRoleIds.contains(id))
                    .collect(Collectors.toSet());
            throw new EntityNotFoundException("Los siguientes roles no existen: " + missingRoleIds);
        }

        addRolesToUser(user, roles);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User removeRolesFromUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));

        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos un rol a eliminar");
        }

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

        // Verificar que todos los roles a eliminar existen en el usuario
        if (rolesToRemove.size() != roleIds.size()) {
            Set<Long> existingRoleIds = rolesToRemove.stream()
                    .map(ur -> ur.getRole().getId())
                    .collect(Collectors.toSet());
            Set<Long> missingRoleIds = roleIds.stream()
                    .filter(id -> !existingRoleIds.contains(id))
                    .collect(Collectors.toSet());
            throw new EntityNotFoundException("El usuario no tiene los siguientes roles: " + missingRoleIds);
        }

        // Eliminar los roles de la colección (orphanRemoval se encargará de la eliminación física)
        rolesToRemove.forEach(userRole -> {
            user.getUserRoles().remove(userRole);
            userRole.setUser(null); // Romper relación bidireccional
        });

        return userRepository.save(user);
    }

    private void addRolesToUser(User user, Set<Role> roles) {
        Set<Long> existingRoleIds = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getId())
                .collect(Collectors.toSet());

        for (Role role : roles) {
            if (existingRoleIds.contains(role.getId())) {
                throw new IllegalArgumentException("El usuario ya tiene asignado el rol: " + role.getName());
            }

            UserRoleId id = new UserRoleId(user.getId(), role.getId());
            UserRole userRole = new UserRole(id, user, role);
            user.getUserRoles().add(userRole);
        }
    }
}
