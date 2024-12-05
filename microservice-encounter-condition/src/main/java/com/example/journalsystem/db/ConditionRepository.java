package com.example.journalsystem.db;

import com.example.journalsystem.bo.model.Condition;
import com.example.journalsystem.bo.model.Patient;
import jakarta.transaction.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
    List<Condition> getConditionByPatient(Patient patient);
}