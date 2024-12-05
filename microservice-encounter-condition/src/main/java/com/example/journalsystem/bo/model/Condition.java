package com.example.journalsystem.bo.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "patient_condition")
@Data
@NoArgsConstructor
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String diagnosis; // The name of the diagnosis


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // Status of the diagnosis (ACTIVE or RESOLVED)

    @Column(nullable = false)
    private Long patientId; // Reference to the associated patient

    // Enum for diagnosis status
    public enum Status {
        ACTIVE, RESOLVED
    }

    // Constructor to initialize the fields
    public Condition(String diagnosis, Status status, Long patientId) {
        this.diagnosis = diagnosis;
        this.status = status;
        this.patientId = patientId;
    }
}
