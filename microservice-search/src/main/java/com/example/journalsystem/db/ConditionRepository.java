package com.example.journalsystem.db;

import com.example.journalsystem.bo.model.Condition;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConditionRepository implements PanacheRepository<Condition> {
}