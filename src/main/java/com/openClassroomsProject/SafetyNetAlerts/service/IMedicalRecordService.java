package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.UniqueIdentifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Service
public interface IMedicalRecordService {

    ArrayList<MedicalRecord> getMedicalRecords();

    boolean addMedicalRecord(MedicalRecord medicalRecord);

    Optional<MedicalRecord> updateAnExistingMedicalRecord(MedicalRecord medicalRecord);

    boolean deleteAMedicalRecord(UniqueIdentifier uniqueIdentifier);
}