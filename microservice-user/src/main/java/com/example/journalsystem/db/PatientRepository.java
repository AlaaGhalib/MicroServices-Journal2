package com.example.journalsystem.db;

import com.example.journalsystem.bo.model.Patient;
import com.example.journalsystem.bo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findPatientById(Long id);
    Optional<Patient> findPatientByName(String username);
    Optional<Patient> findPatientByUser(User user);

}
