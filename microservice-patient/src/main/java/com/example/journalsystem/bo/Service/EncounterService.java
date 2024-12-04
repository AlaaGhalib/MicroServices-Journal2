package com.example.journalsystem.bo.Service;

import com.example.journalsystem.bo.model.Encounter;
import com.example.journalsystem.bo.model.Patient;
import com.example.journalsystem.db.EncounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EncounterService {

    @Autowired
    private EncounterRepository encounterRepository;

    public List<Encounter> getEncountersByPatient(Patient patient) {
        return encounterRepository.findByPatient(patient);
    }

    public Optional<Encounter> findEncounterById(Long id) {
        return encounterRepository.findById(id);
    }

    public Encounter createEncounter(Encounter encounter) {
        return encounterRepository.save(encounter);
    }
}
