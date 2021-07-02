package com.openClassroomsProject.SafetyNetAlerts.service.impl;

import com.openClassroomsProject.SafetyNetAlerts.exception.CustomGenericException;
import com.openClassroomsProject.SafetyNetAlerts.model.*;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.Children;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.ChildrenAndOtherMembers;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonInformation;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.PersonRepository;
import com.openClassroomsProject.SafetyNetAlerts.service.IPersonService;
import com.openClassroomsProject.SafetyNetAlerts.service.helper.AgeCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Primary
@Service
@Slf4j
public class PersonServiceImpl implements IPersonService {
    @Autowired
    private final PersonRepository personRepository;
    @Autowired
    private final MedicalRecordRepository medicalRecordRepository;

    public PersonServiceImpl(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public ArrayList<Person> getPersons() {
        try {
            return personRepository.findAll();
        } catch (Exception e) {
            System.out.println("Error attempting to get persons in [PersonServiceImpl/getPersons] method");
        }
        return null;
    }

    @Override
    public Optional<Person> addNewPerson(Person person) {
        try {
            Optional<Person> personToCreate = personRepository.findPersonByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            if (personToCreate.isPresent()){
                return Optional.empty();
            }
            personRepository.save(person);
        } catch (Exception e) {
            System.out.println("Error attempting to add a new person in [PersonServiceImpl/addNewPerson] method");
        }
        return Optional.of(person);
    }

    @Override
    public Optional<Person> updateAnExistingPerson(Person person) {
        try {
            Optional<Person> personToUpdate = personRepository.findPersonByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            if (personToUpdate.isPresent()) {
                Person personUpdated = personToUpdate.get();
                personUpdated.setAddress(person.getAddress());
                personUpdated.setCity(person.getCity());
                personUpdated.setZip(person.getZip());
                personUpdated.setPhone(person.getPhone());
                personUpdated.setEmail(person.getEmail());
                personRepository.save(personUpdated);
                return Optional.of(personUpdated);
            }
        } catch (Exception e) {
            System.out.println("Error attempting to update an existing person in [PersonServiceImpl/updateAnExistingPerson] method");
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteAPerson(UniqueIdentifier uniqueIdentifier) {
        try {
            Optional<Person> personToDelete = personRepository.findPersonByFirstNameAndLastName(uniqueIdentifier.getFirstName(), uniqueIdentifier.getLastName());
            if (personToDelete.isPresent()) {
                Person personDelete = personToDelete.get();
                personRepository.delete(personDelete);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error attempting to delete a person in [PersonServiceImpl/deleteAPerson] method");
        }
        return false;
    }

    @Override
    public ArrayList<String> getEmailsOfCityDwellers(String city) throws CustomGenericException {
        log.debug("Entered into PersonServiceImpl.getEmailsOfCityDwellers method");
        ArrayList<Person> personsWhoLiveInThisCity = personRepository.findPersonByCity(city);
        ArrayList<String> emailListOfPersonsWhoLiveInThisCity = new ArrayList<>();
        if (personsWhoLiveInThisCity.isEmpty()) {
            return emailListOfPersonsWhoLiveInThisCity;
        }
        for (Person person : personsWhoLiveInThisCity) {
            emailListOfPersonsWhoLiveInThisCity.add(person.getEmail());
        }
        return emailListOfPersonsWhoLiveInThisCity;
    }

    @Override
    public Optional<PersonInformation> getPersonInformation(String firstName, String lastName) {
        log.debug("Entered into PersonServiceImpl.getPersonInformation method");
        Optional<Person> person = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
        if (person.isEmpty()) {
            return Optional.empty();
        }
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(firstName, lastName);
        PersonInformation personInformation = new PersonInformation();
        if (medicalRecord.isPresent()){
            AgeCalculationModel ageCalculationModel = new AgeCalculationModel(medicalRecord.get().getBirthdate(), "MM/dd/yyyy");
            personInformation.setAge(String.valueOf(AgeCalculator.ageCalculator(ageCalculationModel)));
            personInformation.setMedications(medicalRecord.get().getMedications());
            personInformation.setAllergies(medicalRecord.get().getAllergies());
        } else {
            personInformation.setMedications(new ArrayList<>());
            personInformation.setAllergies(new ArrayList<>());
            personInformation.setAge("?");
        }
        personInformation.setFirstName(person.get().getFirstName());
        personInformation.setLastName(person.get().getLastName());
        personInformation.setAddress(person.get().getAddress());
        personInformation.setEmail(person.get().getEmail());
        return Optional.of(personInformation);
    }

    @Override
    public Optional<ChildrenAndOtherMembers> getListOfChildrenLivingAtThisAddress(String address) {
        ArrayList<Person> personAtThisAddress = personRepository.findPersonByAddress(address);
        if (personAtThisAddress.isEmpty()) {
            return Optional.empty();
        }
        ChildrenAndOtherMembers childrenAndOtherMembers = buildChildrenAndOtherMembers(personAtThisAddress);
        if (childrenAndOtherMembers.getChildrenArrayList().isEmpty()) {
            return Optional.of(new ChildrenAndOtherMembers(new ArrayList<>(), new ArrayList<>()));
        }
        return Optional.of(childrenAndOtherMembers);
    }

    @Override
    public ChildrenAndOtherMembers buildChildrenAndOtherMembers(ArrayList<Person> personAtThisAddress) {
        ArrayList<Children> childrenArrayList = new ArrayList<>();
        ArrayList<UniqueIdentifier> uniqueIdentifierArrayList = new ArrayList<>();
        for (Person person : personAtThisAddress) {
            Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            int age = -1;
            if (medicalRecord.isPresent()) {
                AgeCalculationModel ageCalculationModel = new AgeCalculationModel(medicalRecord.get().getBirthdate(), "MM/dd/yyyy");
                age = AgeCalculator.ageCalculator(ageCalculationModel);
            }
            if (age < 19 && age != -1) {
                childrenArrayList.add(new Children(person.getFirstName(), person.getLastName(), String.valueOf(age)));
            } else {
                uniqueIdentifierArrayList.add(new UniqueIdentifier(person.getFirstName(), person.getLastName()));
            }
        }
        return new ChildrenAndOtherMembers(childrenArrayList, uniqueIdentifierArrayList);
    }
}