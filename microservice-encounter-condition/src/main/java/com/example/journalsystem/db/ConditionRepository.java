package com.example.journalsystem.db;

import com.example.journalsystem.bo.model.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
    List<Condition> getConditionByPatientId(Long patientId);
}