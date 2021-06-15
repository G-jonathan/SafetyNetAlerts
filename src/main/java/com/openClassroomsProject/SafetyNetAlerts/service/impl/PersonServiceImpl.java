package com.openClassroomsProject.SafetyNetAlerts.service.impl;

import com.openClassroomsProject.SafetyNetAlerts.model.Person;
import com.openClassroomsProject.SafetyNetAlerts.repository.PersonRepository;
import com.openClassroomsProject.SafetyNetAlerts.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Primary
@Service
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
    public boolean deleteAPerson(String firstName, String lastName) {
        try {
            Optional<Person> personToDelete = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
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
}