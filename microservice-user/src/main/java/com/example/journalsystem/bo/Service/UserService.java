package com.example.journalsystem.bo.Service;

import com.example.journalsystem.bo.model.Role;
import com.example.journalsystem.bo.model.User;
import com.example.journalsystem.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new user, encoding the password before saving.
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Find a user by username.
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserByPhoneNumber(String number) {
        return userRepository.findByPhoneNumber(number);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Find users by their role type
     *
     */
    public List<User> findByRole(Role role) {
        return userRepository.findAllByRole(role);
    }
     /**
     * Authenticate a user by username and password.
     */
    public boolean authenticateUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return password.equals(user.getPassword());
        }
        return false;
    }
    public Optional<User> getPractitionerByUsername(String username) {
        List<Role> practitionerRoles = List.of(Role.DOCTOR, Role.STAFF);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && practitionerRoles.contains(user.get().getRole())) {
            return user;
        }
        return Optional.empty();
    }
}