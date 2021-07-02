package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.exception.CustomGenericException;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.ChildrenAndOtherMembers;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonInformation;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.UniqueIdentifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Service
public interface IPersonService {

    ChildrenAndOtherMembers buildChildrenAndOtherMembers(ArrayList<Person> personAtThisAddress);

    ArrayList<Person> getPersons();

    Optional<Person> addNewPerson(Person person);

    Optional<Person> updateAnExistingPerson(Person person);

    boolean deleteAPerson(UniqueIdentifier uniqueIdentifier);

    ArrayList<String> getEmailsOfCityDwellers(String city) throws CustomGenericException;

    Optional<PersonInformation> getPersonInformation(String firstName, String lastName);

    Optional<ChildrenAndOtherMembers> getListOfChildrenLivingAtThisAddress(String address);
}