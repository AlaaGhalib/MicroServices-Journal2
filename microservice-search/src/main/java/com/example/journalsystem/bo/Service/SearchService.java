package com.example.journalsystem.bo.Service;

import com.example.journalsystem.bo.model.Encounter;
import com.example.journalsystem.bo.model.Role;
import com.example.journalsystem.bo.model.User;
import com.example.journalsystem.bo.model.Condition;
import com.example.journalsystem.db.EncounterRepository;
import com.example.journalsystem.db.UserRepository;
import com.example.journalsystem.db.ConditionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SearchService {

    @Inject
    UserRepository userRepository;

    @Inject
    EncounterRepository encounterRepository;

    @Inject
    ConditionRepository conditionRepository;
    public List<User> getAllPatients() {
        return userRepository.find("role", Role.PATIENT).list();
    }

    // Search patients by first name
    public List<User> searchPatientsByFirstName(String firstName) {
        return userRepository.find("role = ?1 and LOWER(name) LIKE LOWER(?2)", Role.PATIENT, firstName + "%").list();
    }

    // Search patients by last name (assuming last name is part of 'name')
    public List<User> searchPatientsByLastName(String lastName) {
        return userRepository.find("role = ?1 and LOWER(name) LIKE LOWER(?2)", Role.PATIENT, "% " + lastName).list();
    }

    // Search patients by condition name
    public List<User> searchPatientsByCondition(String conditionName) {
        List<Long> patientIds = conditionRepository.find("diagnosis", conditionName).stream()
                .map(Condition::getPatientId)
                .collect(Collectors.toList());
        return userRepository.find("role = ?1 and id IN ?2", Role.PATIENT, patientIds).list();
    }

    public List<User> searchPatientsByObservation(String observationDetail) {
        List<Long> patientIds = encounterRepository.find("notes LIKE ?1", "%" + observationDetail + "%").stream()
                .map(Encounter::getPatientId)
                .collect(Collectors.toList());
        return userRepository.find("role = ?1 and id IN ?2", Role.PATIENT, patientIds).list();
    }

    public List<Encounter> searchEncounters(Long userId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return encounterRepository.find(
                        "patientId IN (SELECT id FROM User WHERE role = 'PATIENT') AND dateTime BETWEEN ?1 AND ?2 AND practitionerId = ?3",
                        startOfDay, endOfDay, userId)
                .list()
                .stream()
                .peek(encounter -> {
                    User patient = userRepository.findById(encounter.getPatientId());
                    if (patient != null) {
                        String[] nameParts = patient.getName().split(" ", 2);
                        if (nameParts.length > 0) encounter.setReason(nameParts[0]); // Example: Store first name in reason
                        if (nameParts.length > 1) encounter.setNotes(nameParts[1]); // Example: Store last name in notes
                    }
                    Condition condition = conditionRepository.find("patientId", encounter.getPatientId()).firstResult();
                    if (condition != null && encounter.getNotes() == null) {
                        encounter.setNotes(condition.getDiagnosis());
                    }
                })
                .collect(Collectors.toList());
    }
}
