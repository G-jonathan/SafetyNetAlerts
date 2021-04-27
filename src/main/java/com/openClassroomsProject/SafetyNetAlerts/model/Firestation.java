package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class Firestation {
    private String address;
    private String station;
}

/*
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

 */
