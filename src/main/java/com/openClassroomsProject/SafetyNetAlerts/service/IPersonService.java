package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.Person;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public interface IPersonService {

    Iterable<Person> getPersons();

    void addNewPerson(Person person);

    Optional<Person> updateAnExistingPerson(Person person);

    boolean deleteAPerson(String firstName, String lastName);
}