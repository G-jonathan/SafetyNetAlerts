package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.Firestation;
import com.openClassroomsProject.SafetyNetAlerts.model.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.Person;
import com.openClassroomsProject.SafetyNetAlerts.repository.FirestationRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.PersonRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class JsonDataService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MedicalRecordRepository medicalrecordRepository;
    @Autowired
    private FirestationRepository firestationRepository;

    public void savePersons(Person[] person) {
        for (Person value : person) {
            personRepository.save(value);
        }
    }

    public void saveFirestations(Firestation[] firestation) {
        for (Firestation value : firestation) {
            firestationRepository.save(value);
        }
    }

    public void saveMedicalRecords(MedicalRecord[] medicalRecord) {
        for (MedicalRecord value : medicalRecord) {
            medicalrecordRepository.save(value);
        }
    }
}