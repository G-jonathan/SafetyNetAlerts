package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="firestation")
public class Firestation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String address;
    private int station;
}
