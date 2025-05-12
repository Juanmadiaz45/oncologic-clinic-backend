package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.dto.registration.RegisterUserDTO;
import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.entity.user.UserRole;
import com.oncologic.clinic.entity.user.UserRole.UserRoleId;
import com.oncologic.clinic.exception.runtime.user.RoleNotFoundException;
import com.oncologic.clinic.exception.runtime.user.UserNotFoundException;
import com.oncologic.clinic.mapper.user.UserMapper;
import com.oncologic.clinic.repository.user.RoleRepository;
import com.oncologic.clinic.repository.user.UserRepository;
import com.oncologic.clinic.repository.user.UserRoleRepository;
import com.oncologic.clinic.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
    public UserResponseDTO createUser(UserDTO userDTO) {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername(userDTO.getUsername());
        registerUserDTO.setPassword(userDTO.getPassword());
        registerUserDTO.setRoleIds(userDTO.getRoleIds());

        User createdUser = this.createUser(registerUserDTO);

        return userMapper.userToUserResponseDto(createdUser);
    }

    @Override
    public User createUser(RegisterUserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("The username is already in use");
        }

        if (userDTO.getRoleIds() == null || userDTO.getRoleIds().isEmpty()) {
            throw new IllegalArgumentException("A user must have at least one role");
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(userDTO.getRoleIds()));
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("The provided roles do not exist");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(user);

        addRolesToUser(savedUser, roles);

        return savedUser;
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserDTO userDTO) {
        if (userDTO.getRoleIds() == null || userDTO.getRoleIds().isEmpty()) {
            throw new IllegalArgumentException("A user must have at least one role");
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!existingUser.getUsername().equals(userDTO.getUsername()) &&
                userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("The username is already in use");
        }

        userMapper.updateUserFromDto(userDTO, existingUser);

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(userDTO.getRoleIds()));
        if (roles.isEmpty() || roles.size() != userDTO.getRoleIds().size()) {
            throw new IllegalArgumentException("One or more provided roles do not exist");
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
                .orElseThrow(() -> new UserNotFoundException(id));

        userRoleRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.userToUserResponseDto(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
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
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("You must provide at least one role");
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));

        if (roles.size() != roleIds.size()) {
            Set<Long> foundRoleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
            Set<Long> missingRoleIds = roleIds.stream()
                    .filter(id -> !foundRoleIds.contains(id))
                    .collect(Collectors.toSet());
            throw new RoleNotFoundException("The following roles do not exist: " + missingRoleIds);
        }

        addRolesToUser(user, roles);
        user = userRepository.save(user);

        return userMapper.userToUserResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDTO removeRolesFromUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("You must provide at least one role to delete");
        }

        // Validate that not all roles are deleted
        long currentRoleCount = user.getUserRoles().size();
        if (currentRoleCount - roleIds.size() <= 0) {
            throw new IllegalStateException("A user must have at least one role");
        }

        // Create a copy to avoid ConcurrentModificationException
        Set<UserRole> rolesToRemove = new HashSet<>();

        // Identify roles to eliminate
        user.getUserRoles().forEach(userRole -> {
            if (roleIds.contains(userRole.getRole().getId())) {
                rolesToRemove.add(userRole);
            }
        });

        // Verify that all roles to be deleted exist for the user
        if (rolesToRemove.size() != roleIds.size()) {
            Set<Long> existingRoleIds = rolesToRemove.stream()
                    .map(ur -> ur.getRole().getId())
                    .collect(Collectors.toSet());
            Set<Long> missingRoleIds = roleIds.stream()
                    .filter(id -> !existingRoleIds.contains(id))
                    .collect(Collectors.toSet());
            throw new UserNotFoundException("The user does not have the following roles: " + missingRoleIds);
        }

        // Remove roles from the collection
        rolesToRemove.forEach(userRole -> {
            user.getUserRoles().remove(userRole);
            userRole.setUser(null); // Breaking a two-way relationship
        });

        userRoleRepository.deleteAll(rolesToRemove);
        User updatedUser = userRepository.save(user);

        return userMapper.userToUserResponseDto(updatedUser);
    }

    @Override
    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
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