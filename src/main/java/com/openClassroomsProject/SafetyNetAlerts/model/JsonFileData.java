package com.openClassroomsProject.SafetyNetAlerts.model;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.FireStation;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import lombok.Data;

@Data
public class JsonFileData {
    private Person[] persons;
    private FireStation[] firestations;
    private MedicalRecord[] medicalrecords;
}