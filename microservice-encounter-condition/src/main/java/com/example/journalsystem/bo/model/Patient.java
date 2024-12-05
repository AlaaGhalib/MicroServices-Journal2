package com.example.journalsystem.bo.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
public class Patient {

    @Id
    @Column(name = "id")
    private Long id; // This ID will be the same as the User's ID

    private String name;
    private String dateOfBirth;
    private String address;

    @OneToOne
    @MapsId // Use the same ID as the User entity
    @JoinColumn(name = "id") // Maps the 'id' field to the User's ID
    private User user;

    public Patient(String name, String dateOfBirth, String address, User user) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.user = user;
        this.id = user.getId(); // Set ID same as User ID
    }
}
