package com.example.journalsystem.db;

import com.example.journalsystem.bo.model.Encounter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EncounterRepository implements PanacheRepository<Encounter> {
}