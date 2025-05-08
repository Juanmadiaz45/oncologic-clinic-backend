package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.dto.user.request.UserRequestDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.dto.user.update.UserUpdateDTO;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.entity.user.UserRole;
import com.oncologic.clinic.entity.user.UserRole.UserRoleId;
import com.oncologic.clinic.mapper.user.UserMapper;
import com.oncologic.clinic.repository.user.RoleRepository;
import com.oncologic.clinic.repository.user.UserRepository;
import com.oncologic.clinic.repository.user.UserRoleRepository;
import com.oncologic.clinic.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        if (userDTO.getRoleIds() == null || userDTO.getRoleIds().isEmpty()) {
            throw new IllegalArgumentException("Un usuario debe tener al menos un rol");
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(userDTO.getRoleIds()));
        if (roles.isEmpty() || roles.size() != userDTO.getRoleIds().size()) {
            throw new IllegalArgumentException("Uno o más roles proporcionados no existen");
        }

        User user = userMapper.userRequestDtoToUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(user);

        addRolesToUser(savedUser, roles);
        savedUser = userRepository.save(savedUser);

        return userMapper.userToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userDTO) {
        if (userDTO.getRoleIds() == null || userDTO.getRoleIds().isEmpty()) {
            throw new IllegalArgumentException("Un usuario debe tener al menos un rol");
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        if (!existingUser.getUsername().equals(userDTO.getUsername()) &&
                userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        userMapper.updateUserFromDto(userDTO, existingUser);

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(userDTO.getRoleIds()));
        if (roles.isEmpty() || roles.size() != userDTO.getRoleIds().size()) {
            throw new IllegalArgumentException("Uno o más roles proporcionados no existen");
        }

        userRoleRepository.deleteByUser(existingUser);
        existingUser.getUserRoles().clear();

        addRolesToUser(existingUser, roles);
        existingUser = userRepository.save(existingUser);

        return userMapper.userToUserResponseDto(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        userRoleRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        return userMapper.userToUserResponseDto(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserResponseDTO> getAllUsersPaginated(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserResponseDto);
    }

    @Override
    public Page<UserResponseDTO> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCaseOrPatient_NameContainingIgnoreCaseOrPersonal_NameContainingIgnoreCase(
                        searchTerm, searchTerm, searchTerm, pageable)
                .map(userMapper::userToUserResponseDto);
    }

    @Override
    @Transactional
    public UserResponseDTO addRolesToUser(Long userId, Set<Long> roleIds) {
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
        user = userRepository.save(user);

        return userMapper.userToUserResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDTO removeRolesFromUser(Long userId, Set<Long> roleIds) {
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

        // Eliminar los roles de la colección
        rolesToRemove.forEach(userRole -> {
            user.getUserRoles().remove(userRole);
            userRole.setUser(null); // Romper relación bidireccional
        });

        userRoleRepository.deleteAll(rolesToRemove);
        User updatedUser = userRepository.save(user);

        return userMapper.userToUserResponseDto(updatedUser);
    }

    @Override
    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con username: " + username));
    }

    @Override
    public List<User> getAllUserEntities() {
        return userRepository.findAll();
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

    @Override
    public Page<User> getAllUserEntitiesPaginated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUserEntities(String searchTerm, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCaseOrPatient_NameContainingIgnoreCaseOrPersonal_NameContainingIgnoreCase(
                searchTerm,
                searchTerm,
                searchTerm,
                pageable
        );
    }
}