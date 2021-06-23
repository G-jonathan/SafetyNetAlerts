package com.openClassroomsProject.SafetyNetAlerts.service.impl;

import com.openClassroomsProject.SafetyNetAlerts.exception.CustomGenericException;
import com.openClassroomsProject.SafetyNetAlerts.model.*;
import com.openClassroomsProject.SafetyNetAlerts.repository.FireStationRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.PersonRepository;
import com.openClassroomsProject.SafetyNetAlerts.service.IFireStationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Primary
@Service
@Slf4j
public class FireStationServiceImpl implements IFireStationService {
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Override
    public Iterable<FireStation> getFireStations() {
        return fireStationRepository.findAll();
    }

    @Override
    public void addStationAndAddressMapping(FireStation fireStation) {
        try {
            fireStationRepository.save(fireStation);
        } catch (Exception e) {
            System.out.println("Error attempting to add data in [addStationAndAddressMapping] method");
        }
    }

    @Override
    public Optional<FireStation> updateFireStationNumberOfAnAddress(FireStation fireStation) {
        try {
            Optional<FireStation> fireStationToUpdate = fireStationRepository.findByAddress(fireStation.getAddress());
            if (fireStationToUpdate.isPresent()) {
                FireStation fireStationUpdated = fireStationToUpdate.get();
                fireStationUpdated.setStation(fireStation.getStation());
                fireStationRepository.save(fireStationUpdated);
                return Optional.of(fireStationUpdated);
            }
        } catch (Exception e) {
            System.out.println("Error attempting to update data in [updateFireStationNumberToAnAddress] method");
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<FireStation>> deleteMappingOfAStation(String stationNumber) {
        Optional<List<FireStation>> fireStationsToDelete = fireStationRepository.findByStation(stationNumber);
        //TODO condition always true
        if (fireStationsToDelete.isPresent()) {
            try {
                List<FireStation> _fireStationsToDelete = fireStationsToDelete.get();
                for (FireStation currentFireStation : _fireStationsToDelete) {
                    fireStationRepository.delete(currentFireStation);
                }
            } catch (Exception e) {
                System.out.println("Error attempting to delete data in [deleteMappingOfAStation] method");
            }
        }
        return fireStationsToDelete;
    }

    @Override
    public Optional<FireStation> deleteMappingOfAnAddress(String address) {
        Optional<FireStation> fireStationToDelete = fireStationRepository.findByAddress(address);
        if (fireStationToDelete.isPresent()) {
            try {
                FireStation _fireStationToDelete = fireStationToDelete.get();
                fireStationRepository.delete(_fireStationToDelete);
            } catch (Exception e) {
                System.out.println("Error attempting to delete data in [deleteMappingOfAnAddress] method");
            }
        }
        return fireStationToDelete;
    }

    @Override
    public ArrayList<String> getPhoneNumbersPersonServedByAFireStation(String fireStationNumber) throws CustomGenericException {
        log.debug("Entered into FireStationServiceImpl.getPhoneNumbersPersonServedByAFireStation method");
        ArrayList<FireStation> fireStationDeserveByThisStation = fireStationRepository.findFireStationByStation(fireStationNumber);
        ArrayList<String> phoneNumbersPersonServedByAFireStation = new ArrayList<>();
        if (fireStationDeserveByThisStation.isEmpty()) {
            return phoneNumbersPersonServedByAFireStation;
        }
        for (FireStation fireStation : fireStationDeserveByThisStation) {
            ArrayList<Person> personWhoLiveAtThisAddress = personRepository.findPersonByAddress(fireStation.getAddress());
            for (Person person : personWhoLiveAtThisAddress) {
                phoneNumbersPersonServedByAFireStation.add(person.getPhone());
            }
        }
        return phoneNumbersPersonServedByAFireStation;
    }

    @Override
    public Optional<PersonListCoveredByAFireStation> getPersonListCoveredByAFireStation(String fireStationNumber) {
        log.debug("Entered into FireStationServiceImpl.getPersonListCoveredByAFireStation method");
        ArrayList<FireStation> fireStationWhoContainsThisFireStationNumber = fireStationRepository.findFireStationByStation(fireStationNumber);
        if (fireStationWhoContainsThisFireStationNumber.isEmpty()) {
            return Optional.empty();
        }
        ArrayList<PersonCoveredByAFireStation> personListCoveredByThisFireStation = new ArrayList<>();
        for (FireStation fireStation : fireStationWhoContainsThisFireStationNumber) {
            ArrayList<Person> personWhoLiveAtThisAddress = personRepository.findPersonByAddress(fireStation.getAddress());
            for (Person person : personWhoLiveAtThisAddress) {
                PersonCoveredByAFireStation personCoveredByAFireStation = new PersonCoveredByAFireStation();
                personCoveredByAFireStation.setFirstName(person.getFirstName());
                personCoveredByAFireStation.setLastName(person.getLastName());
                personCoveredByAFireStation.setAddress(person.getAddress());
                personCoveredByAFireStation.setPhoneNumber(person.getPhone());
                personListCoveredByThisFireStation.add(personCoveredByAFireStation);
            }
        }
        ArrayList<UniqueIdentifier> uniqueIdentifierArrayList = new ArrayList<>();
        for (PersonCoveredByAFireStation person : personListCoveredByThisFireStation) {
            UniqueIdentifier uniqueIdentifier =new UniqueIdentifier(person.getFirstName(), person.getLastName());
            uniqueIdentifierArrayList.add(uniqueIdentifier);
        }
        HashMap<String, String> numberOfAdultAndChildrenHashMap = calculate(uniqueIdentifierArrayList);
        PersonListCoveredByAFireStation personListCoveredByAFireStation = new PersonListCoveredByAFireStation(personListCoveredByThisFireStation, numberOfAdultAndChildrenHashMap);
        return Optional.of(personListCoveredByAFireStation);
    }

    public HashMap<String, String> calculate (ArrayList<UniqueIdentifier> uniqueIdentifierArrayList) {
        log.debug("Entered into CalculationOfTheNumberOfAdultsAndChildren.calculate method");
        int adults = 0;
        int children = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate now = LocalDate.now();
        HashMap<String, String> numberOfAdultAndChildrenHashMap = new HashMap<>();
        for (UniqueIdentifier uniqueIdentifier : uniqueIdentifierArrayList) {
            Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(uniqueIdentifier.getFirstName(), uniqueIdentifier.getLastName());
            if (medicalRecord.isPresent()) {
                String birthdate = medicalRecord.get().getBirthdate();
                LocalDate birthDateConverted = LocalDate.parse(birthdate, formatter);
                long duration = ChronoUnit.DAYS.between(birthDateConverted, now);
                int age = (int) (duration/365);
                if (age > 19) {
                    adults++;
                } else {
                    children++;
                }
            }
        }
        numberOfAdultAndChildrenHashMap.put("Adults", String.valueOf(adults));
        numberOfAdultAndChildrenHashMap.put("Children", String.valueOf(children));
        return numberOfAdultAndChildrenHashMap;
    }
}