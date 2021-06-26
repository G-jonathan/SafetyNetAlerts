package com.openClassroomsProject.SafetyNetAlerts.service.impl;

import com.openClassroomsProject.SafetyNetAlerts.exception.CustomGenericException;
import com.openClassroomsProject.SafetyNetAlerts.model.*;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.FireStation;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.*;
import com.openClassroomsProject.SafetyNetAlerts.repository.FireStationRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.PersonRepository;
import com.openClassroomsProject.SafetyNetAlerts.service.IFireStationService;
import com.openClassroomsProject.SafetyNetAlerts.service.helper.AgeCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
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
            UniqueIdentifier uniqueIdentifier = new UniqueIdentifier(person.getFirstName(), person.getLastName());
            uniqueIdentifierArrayList.add(uniqueIdentifier);
        }
        HashMap<String, String> numberOfAdultAndChildrenHashMap = calculationOfTheNumberOfAdultsAndChildren(uniqueIdentifierArrayList);
        PersonListCoveredByAFireStation personListCoveredByAFireStation = new PersonListCoveredByAFireStation(personListCoveredByThisFireStation, numberOfAdultAndChildrenHashMap);
        return Optional.of(personListCoveredByAFireStation);
    }

    public HashMap<String, String> calculationOfTheNumberOfAdultsAndChildren(ArrayList<UniqueIdentifier> uniqueIdentifierArrayList) {
        log.debug("Entered into FireStationServiceImpl.calculationOfTheNumberOfAdultsAndChildren method");
        int adults = 0;
        int children = 0;
        HashMap<String, String> numberOfAdultAndChildrenHashMap = new HashMap<>();
        numberOfAdultAndChildrenHashMap.put("Adults", "null");
        numberOfAdultAndChildrenHashMap.put("Children", "null");
        for (UniqueIdentifier uniqueIdentifier : uniqueIdentifierArrayList) {
            Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(uniqueIdentifier.getFirstName(), uniqueIdentifier.getLastName());
            if (medicalRecord.isPresent()) {
                String birthDate = medicalRecord.get().getBirthdate();
                AgeCalculationModel ageCalculationModel = new AgeCalculationModel(birthDate, "MM/dd/yyyy");
                int age = AgeCalculator.ageCalculator(ageCalculationModel);
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

    @Override
    public ArrayList<PersonAndFireStationNumberWhoServedHim> getPersonListAndHerFireStationNumber(String address) {
        log.debug("Entered into FireStationServiceImpl.getPersonListAndHerFireStationNumber method");
        ArrayList<Person> personWhoLiveAtThisAddress = personRepository.findPersonByAddress(address);
        ArrayList<PersonAndFireStationNumberWhoServedHim> personAndFireStationNumberWhoServedHimArrayList = new ArrayList<>();
        for (Person person : personWhoLiveAtThisAddress) {
            Optional<FireStation> fireStation = fireStationRepository.findByAddress(person.getAddress());
            String fireStationAddress = fireStation.get().getStation();
            Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            AgeCalculationModel ageCalculationModel = new AgeCalculationModel(medicalRecord.get().getBirthdate(), "MM/dd/yyyy");
            String age = String.valueOf(AgeCalculator.ageCalculator(ageCalculationModel));
            PersonAndFireStationNumberWhoServedHim personAndFireStationNumberWhoServedHim = new PersonAndFireStationNumberWhoServedHim();
            personAndFireStationNumberWhoServedHim.setFirstName(person.getFirstName());
            personAndFireStationNumberWhoServedHim.setLastName(person.getLastName());
            personAndFireStationNumberWhoServedHim.setPhoneNumber(person.getPhone());
            personAndFireStationNumberWhoServedHim.setAge(age);
            personAndFireStationNumberWhoServedHim.setMedication(medicalRecord.get().getMedications());
            personAndFireStationNumberWhoServedHim.setAllergies(medicalRecord.get().getAllergies());
            personAndFireStationNumberWhoServedHim.setFireStationNumber(fireStationAddress);
            personAndFireStationNumberWhoServedHimArrayList.add(personAndFireStationNumberWhoServedHim);
        }
        return personAndFireStationNumberWhoServedHimArrayList;
    }

    @Override
    public ArrayList<HouseHold> getListOfHomesServedByThisStations(ArrayList<String> stationsNumbers) {
        log.debug("Entered into FireStationServiceImpl.getListOfHomesServedByThisStations method");
        ArrayList<HouseHold> houseHoldArrayList = new ArrayList<>();
        for (String station : stationsNumbers) {
            ArrayList<FireStation> fireStationWhoContainsThisFireStationNumber = fireStationRepository.findFireStationByStation(station);
            for (FireStation fireStation : fireStationWhoContainsThisFireStationNumber) {
                ArrayList<Person> personWhoLiveAtThisAddress = personRepository.findPersonByAddress(fireStation.getAddress());
                ArrayList<HouseHoldMember> houseHoldMemberArrayList = buildHouseHoldMemberArrayList(personWhoLiveAtThisAddress);
                HouseHold houseHold = new HouseHold(fireStation.getAddress(), houseHoldMemberArrayList);
                houseHoldArrayList.add(houseHold);
            }
        }
        return houseHoldArrayList;
    }

    public ArrayList<HouseHoldMember> buildHouseHoldMemberArrayList(ArrayList<Person> personWhoLiveAtTheSameAddress) {
        log.debug("Entered into FireStationServiceImpl.buildHouseHoldMemberArrayList method");
        ArrayList<HouseHoldMember> houseHoldMemberArrayList = new ArrayList<>();
        for (Person person : personWhoLiveAtTheSameAddress) {
            HouseHoldMember houseHoldMember = new HouseHoldMember();
            Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            AgeCalculationModel ageCalculationModel = new AgeCalculationModel(medicalRecord.get().getBirthdate(), "MM/dd/yyyy");
            String age = String.valueOf(AgeCalculator.ageCalculator(ageCalculationModel));
            houseHoldMember.setFirstName(person.getFirstName());
            houseHoldMember.setLastName(person.getLastName());
            houseHoldMember.setPhoneNumber(person.getPhone());
            houseHoldMember.setAge(age);
            houseHoldMember.setMedication(medicalRecord.get().getMedications());
            houseHoldMember.setAllergies(medicalRecord.get().getAllergies());
            houseHoldMemberArrayList.add(houseHoldMember);
        }
        return houseHoldMemberArrayList;
    }
}