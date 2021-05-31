package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name="firestation")
public class FireStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "address is mandatory")
    private String address;
    @NotNull(message = "station number is mandatory")
    private String station;
}