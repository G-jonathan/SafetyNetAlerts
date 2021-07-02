package com.openClassroomsProject.SafetyNetAlerts.service.impl;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.PersonRepository;
import com.openClassroomsProject.SafetyNetAlerts.service.IMedicalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Primary
@Service
@Slf4j
public class MedicalRecordServiceImpl implements IMedicalRecordService {
    @Autowired
    private final MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private final PersonRepository personRepository;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository, PersonRepository personRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.personRepository = personRepository;
    }

    @Override
    public ArrayList<MedicalRecord> getMedicalRecords() {
        try {
            return medicalRecordRepository.findAll();
        } catch (Exception e) {
            System.out.println("Error attempting to get medical records in [MedicalRecordServiceImpl/getMedicalRecords] method");
        }
        return null;
    }

    @Override
    public boolean addMedicalRecord(MedicalRecord medicalRecord) {
        log.debug("Entered into MedicalRecordServiceImpl.addMedicalRecord method");
        try {
            Optional<MedicalRecord> isMedicalRecordExist = medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
            Optional<Person> isPersonExist = personRepository.findPersonByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
            if (isMedicalRecordExist.isPresent() || isPersonExist.isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            log.error("Error attempting to add a medical records in [MedicalRecordServiceImpl.addMedicalRecord] method");
        }
        medicalRecordRepository.save(medicalRecord);
        return true;
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