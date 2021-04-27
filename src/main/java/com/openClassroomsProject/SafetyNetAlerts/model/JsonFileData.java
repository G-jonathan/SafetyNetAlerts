package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class JsonFileData {
    private Person[] persons;
    private Firestation firestations;
    private MedicalRecord medicalRecords;
}