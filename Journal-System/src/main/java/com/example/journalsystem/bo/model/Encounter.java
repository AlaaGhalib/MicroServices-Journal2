package com.example.journalsystem.bo.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Encounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private String notes; // This replaces observations list with a single "notes" column

    public Encounter(LocalDateTime dateTime, String reason, Patient patient, String notes) {
        this.dateTime = dateTime;
        this.reason = reason;
        this.patient = patient;
        this.notes = notes;
    }
}
