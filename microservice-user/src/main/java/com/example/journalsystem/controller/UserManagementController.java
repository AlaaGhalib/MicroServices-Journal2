package com.example.journalsystem.controller;

import com.example.journalsystem.bo.Service.UserService;
import com.example.journalsystem.bo.model.Role;
import com.example.journalsystem.bo.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserManagementController {

    private final UserService userService;

    @Autowired
    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @Data
    public static class LoginDTO {
        private String username;
        private String password;
    }

    @Data
    public static class RegisterUserDTO {
        private String username;
        private String password;
        private String phoneNumber;
        private Role role;
        private String name;
        private String dateOfBirth;
        private String address;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginRequest, HttpSession session) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (userService.authenticateUser(username, password)) {
            Optional<User> userOptional = userService.findUserByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                session.setAttribute("username", user.getUsername());
                Map<String, Object> response = Map.of(
                        "message", "Login successful",
                        "role", user.getRole().toString(),
                        "username", user.getUsername(),
                        "id", user.getId()
                );
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDTO registerRequest) {
        if (userService.findUserByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }

        if (userService.findUserByPhoneNumber(registerRequest.getPhoneNumber()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Phone number already exists.");
        }
        try {
            User newUser = new User(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getPhoneNumber(),
                    registerRequest.getRole(),
                    registerRequest.getName(),
                    registerRequest.getDateOfBirth(),
                    registerRequest.getAddress()
            );
            User savedUser = userService.createUser(newUser);
            Map<String, Object> response = Map.of(
                    "message", "User registered successfully",
                    "userDetails", Map.of(
                            "username", savedUser.getUsername(),
                            "name", savedUser.getName(),
                            "address", savedUser.getAddress(),
                            "phoneNumber", savedUser.getPhoneNumber(),
                            "dateOfBirth", savedUser.getDateOfBirth(),
                            "role", savedUser.getRole().toString()
                    )
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user. Please try again.");
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader(value = "Username", required = false) String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username header is required.");
        }

        Optional<User> userOptional = userService.findUserByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOptional.get();
        Map<String, Object> responseData = Map.of(
                "username", user.getUsername(),
                "name", user.getName(),
                "address", user.getAddress(),
                "phoneNumber", user.getPhoneNumber(),
                "dateOfBirth", user.getDateOfBirth(),
                "role", user.getRole().toString()
        );

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/details/{userId}")
    public ResponseEntity<?> getUserDetailsById(@PathVariable Long userId) {
        Optional<User> userOptional = userService.findUserById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        User user = userOptional.get();

        Map<String, Object> responseData = Map.of(
                "username", user.getUsername(),
                "name", user.getName(),
                "address", user.getAddress(),
                "phoneNumber", user.getPhoneNumber(),
                "dateOfBirth", user.getDateOfBirth(),
                "role", user.getRole().toString()
        );
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/patients")
    public ResponseEntity<List<User>> getAllPatients() {
        List<User> patients = userService.findByRole(Role.PATIENT);
        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getPractitionerByUsername(@PathVariable String username) {
        Optional<User> practitioner = userService.getPractitionerByUsername(username);
        return practitioner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }
}
