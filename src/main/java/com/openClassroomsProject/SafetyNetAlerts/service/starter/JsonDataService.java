package com.openClassroomsProject.SafetyNetAlerts.service.starter;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.FireStation;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.repository.FireStationRepository;
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
    private FireStationRepository firestationRepository;

    public void savePersons(Person[] person) {
        for (Person currentPerson : person) {
            personRepository.save(currentPerson);
        }
    }

    public void saveFireStations(FireStation[] firestation) {
        for (FireStation currentFireStation : firestation) {
            firestationRepository.save(currentFireStation);
        }
    }

    public void saveMedicalRecords(MedicalRecord[] medicalRecord) {
        for (MedicalRecord currentMedicalRecord : medicalRecord) {
            medicalrecordRepository.save(currentMedicalRecord);
        }
    }
}