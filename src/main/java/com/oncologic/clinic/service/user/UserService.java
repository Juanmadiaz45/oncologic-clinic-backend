package com.oncologic.clinic.service.user;

import com.oncologic.clinic.dto.user.request.UserRequestDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.dto.user.update.UserUpdateDTO;
import com.oncologic.clinic.entity.user.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userDTO);
    UserResponseDTO updateUser(Long id, UserUpdateDTO userDTO);
    void deleteUser(Long id);
    UserResponseDTO getUserById(Long id);
    User getUserByUsername(String username);
    List<UserResponseDTO> getAllUsers();
    Page<UserResponseDTO> getAllUsersPaginated(Pageable pageable);
    Page<UserResponseDTO> searchUsers(String searchTerm, Pageable pageable);

    UserResponseDTO addRolesToUser(Long userId, Set<Long> roleIds);
    UserResponseDTO removeRolesFromUser(Long userId, Set<Long> roleIds);

    User getUserEntityById(Long id);
    User getUserEntityByUsername(String username);
    List<User> getAllUserEntities();
    Page<User> getAllUserEntitiesPaginated(Pageable pageable);
    Page<User> searchUserEntities(String searchTerm, Pageable pageable);
}