package com.oncologic.clinic.repository.user;

import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {
    List<UserRole> findByUser(User user);
    void deleteByUser(User user);
    long countByUser(User user);
}
