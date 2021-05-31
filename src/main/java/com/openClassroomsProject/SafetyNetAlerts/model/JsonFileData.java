package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class JsonFileData {
    private Person[] persons;
    private FireStation[] firestations;
    private MedicalRecord[] medicalrecords;
}