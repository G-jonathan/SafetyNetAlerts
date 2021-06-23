package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.UniqueIdentifier;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public interface IMedicalRecordService {

    Iterable<MedicalRecord> getMedicalRecords();

    void addMedicalRecord(MedicalRecord medicalRecord);

    Optional<MedicalRecord> updateAnExistingMedicalRecord(MedicalRecord medicalRecord);

    boolean deleteAMedicalRecord(UniqueIdentifier uniqueIdentifier);
}