package com.openClassroomsProject.SafetyNetAlerts.service.impl;

import com.openClassroomsProject.SafetyNetAlerts.exception.CustomGenericException;
import com.openClassroomsProject.SafetyNetAlerts.model.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.repository.PersonRepository;
import com.openClassroomsProject.SafetyNetAlerts.service.IPersonService;
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
    private PersonRepository personRepository;

    @Override
    public Iterable<Person> getPersons() {
        try {
            return personRepository.findAll();
        } catch (Exception e) {
            System.out.println("Error attempting to get persons in [PersonServiceImpl/getPersons] method");
        }
        return null;
    }

    @Override
    public void addNewPerson(Person person) {
        try {
            personRepository.save(person);
        } catch (Exception e) {
            System.out.println("Error attempting to add a new person in [PersonServiceImpl/addNewPerson] method");
        }
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
}