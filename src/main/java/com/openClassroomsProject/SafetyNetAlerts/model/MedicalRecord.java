package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="medicalrecord")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    private String birthdate;
    @ElementCollection
    private List<String> medications;
    @ElementCollection
    private List<String> allergies;
}