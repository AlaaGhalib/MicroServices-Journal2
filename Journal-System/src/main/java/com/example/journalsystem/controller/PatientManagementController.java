package com.example.journalsystem.controller;

import com.example.journalsystem.bo.Service.PatientService;
import com.example.journalsystem.bo.Service.UserService;
import com.example.journalsystem.bo.model.Patient;
import com.example.journalsystem.bo.model.Role;
import com.example.journalsystem.bo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:3000")
public class PatientManagementController {
    private final UserService userService;
    private final PatientService patientService;

    @Autowired
    public PatientManagementController(UserService userService, PatientService patientService) {
        this.userService = userService;
        this.patientService = patientService;
    }

    @GetMapping("/details")
    public ResponseEntity<?> getPatientDetails(@RequestHeader("Username") String username) {
        Optional<User> userOptional = userService.findUserByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        User user = userOptional.get();

        Optional<Patient> patientOptional = patientService.findPatientByUser(user);
        if (patientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient details not found.");
        }
        Patient patient = patientOptional.get();
        Map<String, Object> responseData = Map.of(
                "username", user.getUsername(),
                "name", patient.getName(),
                "address", patient.getAddress(),
                "phoneNumber", user.getPhoneNumber(),
                "dateOfBirth", patient.getDateOfBirth()
        );
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/details/{patientId}")
    public ResponseEntity<?> getPatientDetailsById(@PathVariable Long patientId) {
        Optional<Patient> patientOptional = patientService.findPatientById(patientId);
        if (patientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
        Patient patient = patientOptional.get();
        User user = patient.getUser();
        Map<String, Object> responseData = Map.of(
                "username", user.getUsername(),
                "name", patient.getName(),
                "address", patient.getAddress(),
                "phoneNumber", user.getPhoneNumber(),
                "dateOfBirth", patient.getDateOfBirth()
        );
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllPatients() {
        List<User> patients = userService.findByRole(Role.PATIENT);
        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(patients);
    }
}
