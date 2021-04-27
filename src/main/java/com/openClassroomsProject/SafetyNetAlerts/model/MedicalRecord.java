package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class MedicalRecord {
    private String firstName;
    private String lastName;
    private String birthdate;
    private String[] medications;
    private String[] allergies;
}

/*
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
    private LocalDate birthdate;
    private String medications;
    private String allergies;
}

 */
