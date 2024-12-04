package com.example.journalsystem.bo.Service;

import com.example.journalsystem.bo.model.Patient;
import com.example.journalsystem.db.PatientRepository;
import com.example.journalsystem.bo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Optional<Patient> findPatientById(Long id) { return patientRepository.findPatientById(id); }
    public Optional<Patient> findPatientByName(String name) { return patientRepository.findPatientByName(name); }
    public Optional<Patient> findPatientByUser(User user) { return patientRepository.findPatientByUser(user); }
}

