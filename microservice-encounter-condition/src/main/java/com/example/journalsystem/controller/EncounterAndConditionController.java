package com.example.journalsystem.controller;
import com.example.journalsystem.bo.Service.*;
import com.example.journalsystem.bo.model.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class EncounterAndConditionController {

    private final UserService userService;
    private final EncounterService encounterService;
    private final ConditionService conditionService;

    @Autowired
    public EncounterAndConditionController(UserService userService, EncounterService encounterService, ConditionService conditionService) {
        this.userService = userService;
        this.encounterService = encounterService;
        this.conditionService = conditionService;
    }

    @Data
    public static class EncounterDTO {
        private Long patientId;
        private String reason;
        private String notes;
    }

    @Data
    public static class ConditionDTO {
        private Long patientId;
        private String diagnosis;
        private Condition.Status status;
    }

    @PostMapping("/encounters/add")
    public ResponseEntity<?> addEncounter(@RequestBody EncounterDTO encounterDTO) {
        try {
            Encounter encounter = new Encounter(
                    LocalDateTime.now(),
                    encounterDTO.getReason(),
                    encounterDTO.getPatientId(),
                    encounterDTO.getNotes()
            );
            Encounter savedEncounter = encounterService.createEncounter(encounter);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEncounter);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add encounter due to server error: " + e.getMessage());
        }
    }


    @PutMapping("/encounters/{encounterId}/update-notes")
    public ResponseEntity<?> updateEncounterNotes(@PathVariable Long encounterId, @RequestBody String notes) {
        Optional<Encounter> encounterOpt = encounterService.findEncounterById(encounterId);
        if (encounterOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Encounter not found.");
        }
        Encounter encounter = encounterOpt.get();
        encounter.setNotes(notes);
        Encounter updatedEncounter = encounterService.createEncounter(encounter);
        return ResponseEntity.ok(updatedEncounter);
    }

    @GetMapping("/encounters/patient/{patientId}")
    public ResponseEntity<?> getEncountersByPatient(@PathVariable Long patientId) {
        Optional<User> patientOpt = userService.findPatientById(patientId);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
        List<Encounter> encounters = encounterService.getEncountersByPatient(patientId);
        return ResponseEntity.ok(encounters);
    }
    @GetMapping("/conditions/show/{patientId}")
    public ResponseEntity<?> getConditionByPatient(@PathVariable Long patientId) {
        Optional<User> patientOpt = userService.findPatientById(patientId);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
        List<Condition> conditions = conditionService.getConditionByPatient(patientId);
        return ResponseEntity.ok(conditions);
    }
    @PostMapping("/conditions/add")
    public ResponseEntity<?> addCondition(@RequestBody ConditionDTO conditionDTO) {
        try {
            Condition condition = new Condition(
                    conditionDTO.getDiagnosis(),
                    conditionDTO.getStatus(),
                    conditionDTO.getPatientId()
            );
            Condition savedCondition = conditionService.createCondition(condition);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCondition);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status value. Please use ACTIVE or RESOLVED.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add condition due to server error: " + e.getMessage());
        }
    }
    @PutMapping("/conditions/update/{conditionId}")
    public ResponseEntity<?> updateCondition(@PathVariable Long conditionId, @RequestBody ConditionDTO conditionDTO) {
        try {
            String newDiagnosis = conditionDTO.getDiagnosis();
            Condition.Status newStatus = conditionDTO.getStatus();
            Condition updatedCondition = conditionService.updateCondition(conditionId, newDiagnosis, newStatus);
            return ResponseEntity.ok(updatedCondition);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status value. Please use ACTIVE or RESOLVED.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating condition: " + e.getMessage());
        }
    }
}

