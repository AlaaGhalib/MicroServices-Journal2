package com.example.journalsystem.bo.Service;

import com.example.journalsystem.bo.model.Condition;
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
    public List<Condition> getConditionByPatientId(Long patientId) {
        return conditionRepository.getConditionByPatientId(patientId);
    }
    public Condition updateCondition(Long conditionId, String diagnosis, Condition.Status status) {
        Optional<Condition> conditionOpt = conditionRepository.findById(conditionId);
        if (conditionOpt.isPresent()) {
            Condition condition = conditionOpt.get();
            if (diagnosis != null && !diagnosis.isEmpty()) {
                condition.setDiagnosis(diagnosis);
            }
            if (status != null) {
                condition.setStatus(status);
            }
            return conditionRepository.save(condition);
        } else {
            throw new EntityNotFoundException("Condition not found with ID: " + conditionId);
        }
    }
}
