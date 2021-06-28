package com.openClassroomsProject.SafetyNetAlerts.model.dbmodel;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@Table(name="medicalrecord")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    @Size(min = 2, max = 40, message = "firstName must be between 2 and 30 characters long.")
    @NotNull(message = "firstName is mandatory")
    private String firstName;
    @Column(name = "last_name")
    @Size(min = 2, max = 40, message = "lastName must be between 2 and 30 characters long.")
    @NotNull(message = "lastName is mandatory")
    private String lastName;
    @NotNull(message = "birthdate is mandatory")
    private String birthdate;
    @ElementCollection
    @NotNull(message = "medications is mandatory (empty if needed)")
    private List<String> medications;
    @ElementCollection
    @NotNull(message = "allergies is mandatory (empty if needed")
    private List<String> allergies;
}