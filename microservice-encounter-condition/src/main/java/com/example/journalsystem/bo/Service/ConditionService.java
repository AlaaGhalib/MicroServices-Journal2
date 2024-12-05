package com.example.journalsystem.bo.Service;

import com.example.journalsystem.bo.model.Condition;
import com.example.journalsystem.bo.model.Patient;
import com.example.journalsystem.db.ConditionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConditionService {

    @Autowired
    private ConditionRepository conditionRepository;

    public Condition createCondition(Condition condition) {
        return conditionRepository.save(condition);
    }
    public List<Condition> getConditionByPatient(Patient patient) {
        return conditionRepository.getConditionByPatient(patient);
    }
    // Update condition method
    public Condition updateCondition(Long conditionId, String diagnosis, Condition.Status status) {
        // Find the existing condition
        Optional<Condition> conditionOpt = conditionRepository.findById(conditionId);

        if (conditionOpt.isPresent()) {
            Condition condition = conditionOpt.get();

            // Update fields if provided
            if (diagnosis != null && !diagnosis.isEmpty()) {
                condition.setDiagnosis(diagnosis);
            }
            if (status != null) {
                condition.setStatus(status);
            }

            // Save the updated condition
            return conditionRepository.save(condition);
        } else {
            throw new EntityNotFoundException("Condition not found with ID: " + conditionId);
        }
    }
}
