package com.example.journalsystem.db;

import com.example.journalsystem.bo.model.Encounter;
import com.example.journalsystem.bo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EncounterRepository extends JpaRepository<Encounter, Long> {
    List<Encounter> findByPatient(Patient patient);
}
