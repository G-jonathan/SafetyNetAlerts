package com.openClassroomsProject.SafetyNetAlerts.service.impl;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openClassroomsProject.SafetyNetAlerts.service.IMedicalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Primary
@Service
@Slf4j
public class MedicalRecordServiceImpl implements IMedicalRecordService {
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Override
    public Iterable<MedicalRecord> getMedicalRecords() {
        try {
            return medicalRecordRepository.findAll();
        } catch (Exception e) {
            System.out.println("Error attempting to get medical records in [MedicalRecordServiceImpl/getMedicalRecords] method");
        }
        return null;
    }

    @Override
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        try {
            medicalRecordRepository.save(medicalRecord);
        } catch (Exception e) {
            System.out.println("Error attempting to add a new person in [MedicalRecordServiceImpl/addMedicalRecord] method");
        }
    }

    @Override
    public Optional<MedicalRecord> updateAnExistingMedicalRecord(MedicalRecord medicalRecord) {
        try {
            Optional<MedicalRecord> medicalRecordToUpdate = medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
            if (medicalRecordToUpdate.isPresent()) {
                MedicalRecord medicalRecordUpdated = medicalRecordToUpdate.get();
                medicalRecordUpdated.setBirthdate(medicalRecord.getBirthdate());
                medicalRecordUpdated.setMedications(medicalRecord.getMedications());
                medicalRecordUpdated.setAllergies(medicalRecord.getAllergies());
                medicalRecordRepository.save(medicalRecordUpdated);
                return Optional.of(medicalRecordUpdated);
            }
        } catch (Exception e) {
            System.out.println("Error attempting to update an existing person in [MedicalRecordServiceImpl/updateAnExistingMedicalRecord] method");
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteAMedicalRecord(UniqueIdentifier uniqueIdentifier) {
        try {
            Optional<MedicalRecord> medicalRecordToDelete = medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(uniqueIdentifier.getFirstName(), uniqueIdentifier.getLastName());
            if (medicalRecordToDelete.isPresent()) {
                MedicalRecord medicalRecordDeleted = medicalRecordToDelete.get();
                medicalRecordRepository.delete(medicalRecordDeleted);
                return true;
            }
        } catch (Exception e) {
            log.error(String.valueOf(e));
            System.out.println("Error attempting to delete a person in [MedicalRecordServiceImpl/deleteAMedicalRecord] method");
        }
        return false;
    }
}