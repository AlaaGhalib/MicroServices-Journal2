package com.example.journalsystem.bo.Service;

import com.example.journalsystem.bo.model.Patient;
import com.example.journalsystem.db.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    public Optional<Patient> findPatientById(Long id) { return patientRepository.findPatientById(id); }
}

