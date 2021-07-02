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
    private final FireStationRepository fireStationRepository;
    @Autowired
    private final PersonRepository personRepository;
    @Autowired
    private final MedicalRecordRepository medicalRecordRepository;

    public FireStationServiceImpl(FireStationRepository fireStationRepository, PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.fireStationRepository = fireStationRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public ArrayList<FireStation> getFireStations() {
        log.debug("Entered into FireStationServiceImpl.getFireStations method");
        return fireStationRepository.findAll();
    }

    @Override
    public Optional<FireStation> addStationAndAddressMapping(FireStation fireStation) throws CustomGenericException {
        log.debug("Entered into FireStationServiceImpl.addStationAndAddressMapping method");
        Optional<FireStation> isFireStationAddressAlreadyExist = fireStationRepository.findByAddress(fireStation.getAddress());
        if (isFireStationAddressAlreadyExist.isEmpty()) {
            fireStationRepository.save(fireStation);
            return Optional.of(fireStation);
        }
        return Optional.empty();
    }

    @Override
    public Optional<FireStation> updateFireStationNumberOfAnAddress(FireStation fireStation) throws CustomGenericException {
        log.debug("Entered into FireStationServiceImpl.updateFireStationNumberOfAnAddress method");
        Optional<FireStation> fireStationToUpdate = fireStationRepository.findByAddress(fireStation.getAddress());
        if (fireStationToUpdate.isPresent()) {
            FireStation fireStationUpdated = fireStationToUpdate.get();
            fireStationUpdated.setStation(fireStation.getStation());
            fireStationRepository.save(fireStationUpdated);
            return Optional.of(fireStationUpdated);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<FireStation>> deleteMappingOfAStation(String stationNumber) throws CustomGenericException {
        log.debug("Entered into FireStationServiceImpl.deleteMappingOfAStation method");
        Optional<List<FireStation>> fireStationsToDelete = fireStationRepository.findByStation(stationNumber);
        if (fireStationsToDelete.isPresent()) {
            List<FireStation> _fireStationsToDelete = fireStationsToDelete.get();
            for (FireStation currentFireStation : _fireStationsToDelete) {
                fireStationRepository.delete(currentFireStation);
            }
            return fireStationsToDelete;
        }
        return Optional.empty();
    }

    @Override
    public Optional<FireStation> deleteMappingOfAnAddress(String address) throws CustomGenericException {
        log.debug("Entered into FireStationServiceImpl.deleteMappingOfAnAddress method");
        Optional<FireStation> fireStationToDelete = fireStationRepository.findByAddress(address);
        if (fireStationToDelete.isPresent()) {
            FireStation _fireStationToDelete = fireStationToDelete.get();
            fireStationRepository.delete(_fireStationToDelete);
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
    public Optional<PersonListCoveredByAFireStation> getPersonListCoveredByAFireStation(String fireStationNumber) throws CustomGenericException {
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

    @Override
    public HashMap<String, String> calculationOfTheNumberOfAdultsAndChildren(ArrayList<UniqueIdentifier> uniqueIdentifierArrayList) throws CustomGenericException {
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
    public ArrayList<PersonAndFireStationNumberWhoServedHim> getPersonListAndHerFireStationNumber(String address) throws CustomGenericException {
        log.debug("Entered into FireStationServiceImpl.getPersonListAndHerFireStationNumber method");
        ArrayList<Person> personWhoLiveAtThisAddress = personRepository.findPersonByAddress(address);
        ArrayList<PersonAndFireStationNumberWhoServedHim> personAndFireStationNumberWhoServedHimArrayList = new ArrayList<>();
        if (personWhoLiveAtThisAddress.isEmpty()) {
            return personAndFireStationNumberWhoServedHimArrayList;
        }
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
    public ArrayList<HouseHold> getListOfHomesServedByThisStations(ArrayList<String> stationsNumbers) throws CustomGenericException {
        log.debug("Entered into FireStationServiceImpl.getListOfHomesServedByThisStations method");
        ArrayList<HouseHold> houseHoldArrayList = new ArrayList<>();
        for (String station : stationsNumbers) {
            ArrayList<FireStation> fireStationWhoContainsThisFireStationNumber = fireStationRepository.findFireStationByStation(station);
            if (!fireStationWhoContainsThisFireStationNumber.isEmpty()) {
                for (FireStation fireStation : fireStationWhoContainsThisFireStationNumber) {
                    ArrayList<Person> personWhoLiveAtThisAddress = personRepository.findPersonByAddress(fireStation.getAddress());
                    ArrayList<HouseHoldMember> houseHoldMemberArrayList = buildHouseHoldMemberArrayList(personWhoLiveAtThisAddress);
                    HouseHold houseHold = new HouseHold(fireStation.getAddress(), houseHoldMemberArrayList);
                    houseHoldArrayList.add(houseHold);
                }
            }
        }
        return houseHoldArrayList;
    }

    @Override
    public ArrayList<HouseHoldMember> buildHouseHoldMemberArrayList(ArrayList<Person> personWhoLiveAtTheSameAddress) throws CustomGenericException {
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