package com.example.journalsystem.db;
import com.example.journalsystem.bo.model.Role;
import com.example.journalsystem.bo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String number);
    List<User> findAllByRole(Role role);
    Optional<User> findById(Long id);
    List<User> findAllByRoleIn(List<Role> roles);
}

