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

    @Column(nullable = false)
    private Long patientId;

    private String notes;

    public Encounter(LocalDateTime dateTime, String reason, Long patientId, String notes) {
        this.dateTime = dateTime;
        this.reason = reason;
        this.patientId = patientId;
        this.notes = notes;
    }
}
