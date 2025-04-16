package com.oncologic.clinic.service.user;

import com.oncologic.clinic.dto.register.RegisterUserDTO;
import com.oncologic.clinic.entity.user.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User registerUser(RegisterUserDTO userDTO);

    User updateUser(User user, Set<Long> roleIds);

    void deleteUser(Long id);

    User getUserById(Long id);

    User getUserByUsername(String username);

    List<User> getAllUsers();

    User addRolesToUser(Long userId, Set<Long> roleIds);

    User removeRolesFromUser(Long userId, Set<Long> roleIds);
}
