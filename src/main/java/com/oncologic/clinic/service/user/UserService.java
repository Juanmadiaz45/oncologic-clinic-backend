package com.oncologic.clinic.service.user;

import com.oncologic.clinic.dto.registration.RegisterUserDTO;
import com.oncologic.clinic.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface UserService {
    User createUser(RegisterUserDTO userDTO);

    User updateUser(User user, Set<Long> roleIds);

    void deleteUser(Long id);

    User getUserById(Long id);

    User getUserByUsername(String username);

    List<User> getAllUsers();

    Page<User> getAllUsersPaginated(Pageable pageable);

    Page<User> searchUsers(String searchTerm, Pageable pageable);

    User addRolesToUser(Long userId, Set<Long> roleIds);

    User removeRolesFromUser(Long userId, Set<Long> roleIds);
}
