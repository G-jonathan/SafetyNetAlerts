package com.openClassroomsProject.SafetyNetAlerts.repository;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    Optional<Person> findPersonByFirstNameAndLastName(String firstName, String lastName);

    ArrayList<Person> findPersonByCity(String city);

    ArrayList<Person> findPersonByAddress(String address);
}