package com.example.journalsystem.db;

import com.example.journalsystem.bo.model.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PracitionerRepository extends JpaRepository<Practitioner, Long> {
}
