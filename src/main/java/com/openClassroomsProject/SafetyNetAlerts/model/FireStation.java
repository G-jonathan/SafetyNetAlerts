package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="firestation")
public class FireStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String address;
    private String station;
}