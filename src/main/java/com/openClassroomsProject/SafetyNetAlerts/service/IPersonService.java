package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.exception.CustomGenericException;
import com.openClassroomsProject.SafetyNetAlerts.model.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.UniqueIdentifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Service
public interface IPersonService {

    Iterable<Person> getPersons();

    void addNewPerson(Person person);

    Optional<Person> updateAnExistingPerson(Person person);

    boolean deleteAPerson(UniqueIdentifier uniqueIdentifier);

    ArrayList<String> getEmailsOfCityDwellers(String city) throws CustomGenericException;
}