package com.openClassroomsProject.SafetyNetAlerts.repository;

import com.openClassroomsProject.SafetyNetAlerts.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person,Long> {

    Optional<Person> findPersonByFirstNameAndLastName(String firstName, String lastName);
}