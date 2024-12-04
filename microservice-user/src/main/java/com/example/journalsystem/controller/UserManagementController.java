package com.example.journalsystem.controller;

import com.example.journalsystem.bo.Service.*;
import com.example.journalsystem.bo.model.*;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserManagementController {
    private final UserService userService;
    private final PractitionerService practitionerService;
    private final PatientService patientService;
    @Autowired
    public UserManagementController(UserService userService, PractitionerService practitionerService, PatientService patientService) {
        this.userService = userService;
        this.practitionerService = practitionerService;
        this.patientService = patientService;
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
        private Role role;
        private String phoneNumber;
    }
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class RegisterPatientDTO extends RegisterUserDTO {
        private String name;
        private String address;
        private String dateOfBirth;
    }
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class RegisterPractitionerDTO extends RegisterUserDTO {
        private String name;
        private String specialty;
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

    @PostMapping("/register/patient")
    public ResponseEntity<?> registerPatient(@RequestBody RegisterPatientDTO registerRequest) {
        if (userService.findUserByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }
        if (userService.findUserByPhoneNumber(registerRequest.getPhoneNumber()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Phone number already exists.");
        }
        if (registerRequest.getRole() != Role.PATIENT) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role for patient registration");
        }
        try {
            User newUser = new User();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setPassword(registerRequest.getPassword());
            newUser.setPhoneNumber(registerRequest.getPhoneNumber());
            newUser.setRole(registerRequest.getRole());

            User savedUser = userService.createUser(newUser);
            Patient patient = new Patient();
            patient.setUser(savedUser);
            patient.setName(registerRequest.getName());
            patient.setAddress(registerRequest.getAddress());
            patient.setDateOfBirth(registerRequest.getDateOfBirth());
            patientService.createPatient(patient);

            Map<String, Object> response = Map.of(
                    "message", "Patient registered successfully",
                    "patientDetails", Map.of(
                            "username", savedUser.getUsername(),
                            "name", patient.getName(),
                            "address", patient.getAddress(),
                            "phoneNumber", savedUser.getPhoneNumber(),
                            "dateOfBirth", patient.getDateOfBirth()
                    )
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register patient. Please try again.");
        }
    }

    @PostMapping("/register/practitioner")
    public ResponseEntity<String> registerPractitioner(@RequestBody RegisterPractitionerDTO registerRequest) {
        if (userService.findUserByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }
        if (userService.findUserByPhoneNumber(registerRequest.getPhoneNumber()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Phone number already exists.");
        }
        if (registerRequest.getRole() == Role.PATIENT) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role for practitioner registration");
        }
        try {
            User newUser = new User();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setPassword(registerRequest.getPassword());
            newUser.setPhoneNumber(registerRequest.getPhoneNumber());
            newUser.setRole(registerRequest.getRole());

            User savedUser = userService.createUser(newUser);
            Practitioner practitioner = new Practitioner();
            practitioner.setUser(savedUser);
            practitioner.setName(registerRequest.getName());
            practitioner.setSpecialty(registerRequest.getSpecialty());
            practitionerService.createPractitioner(practitioner);

            return ResponseEntity.status(HttpStatus.CREATED).body("Practitioner registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register practitioner. Please try again.");
        }
    }
}
