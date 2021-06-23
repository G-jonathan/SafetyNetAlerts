package com.openClassroomsProject.SafetyNetAlerts.controller;

import com.openClassroomsProject.SafetyNetAlerts.exception.CustomGenericException;
import com.openClassroomsProject.SafetyNetAlerts.exception.ResourceNotFoundException;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@Slf4j
public class PersonController {
    @Autowired
    private IPersonService personService;
    private static final String CLASSPATH = "com.openClassroomsProject.SafetyNetAlerts.controller.PersonController";



    @GetMapping("/communityEmail")
    public ResponseEntity<ArrayList<String>> getEmailsOfCityDwellers(@RequestParam String city) {
        String functionPath = CLASSPATH + ".getEmailsFromCityDwellers";
        log.info("Request received in " + functionPath);
        ArrayList<String> requestContent;
        try {
            requestContent = personService.getEmailsOfCityDwellers(city);
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        if (requestContent.isEmpty()) {
            throw new ResourceNotFoundException(functionPath, "Nothing found for this city");
        }
        log.info("Request success in " + functionPath);
        return new ResponseEntity<>(requestContent, HttpStatus.OK);
    }

    @GetMapping("/person")
    public Iterable<Person> getPersons() {
        String functionPath = CLASSPATH + ".getPersons";
        log.info("Request received in " + functionPath);
        Iterable<Person> requestContent;
        try {
            requestContent = personService.getPersons();
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        log.info("Request success in " + functionPath);
        return requestContent;
    }

    @PostMapping("/person")
    public ResponseEntity<?> addNewPerson(@Valid @RequestBody Person person) {
        String functionPath = CLASSPATH + ".addNewPerson";
        log.info("Request received in " + functionPath);
        try {
            personService.addNewPerson(person);
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        log.info("Request success in " + functionPath);
        return new ResponseEntity<>(person + "\n" + " --> has been successfully created", HttpStatus.CREATED);
    }

    @PutMapping("/person")
    public ResponseEntity<?> updateAnExistingPerson(@Valid @RequestBody Person person) {
        String functionPath = CLASSPATH + ".updateAnExistingPerson";
        log.info("Request received in " + functionPath);
        try {
            Optional<Person> personUpdated = personService.updateAnExistingPerson(person);
            if (personUpdated.isPresent()) {
                log.info("Request success. Person successfully modified: ");
                return new ResponseEntity<>(personUpdated + "\n" + " --> has been successfully modified", HttpStatus.OK);
            }
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        throw new ResourceNotFoundException(functionPath, "Person not found");
    }

    @DeleteMapping("/person")
    public ResponseEntity<?> deleteAPerson(@Valid @RequestBody UniqueIdentifier uniqueIdentifier) {
        String functionPath = CLASSPATH + ".deleteAPerson";
        log.info("Request received in " + functionPath);
        try {
            boolean isSuccess = personService.deleteAPerson(uniqueIdentifier);
            if (isSuccess) {
                log.info("Request success in " + functionPath + ". " + uniqueIdentifier + "'s Medical record deleted: ");
                return new ResponseEntity<>(uniqueIdentifier + " person has been successfully deleted", HttpStatus.OK);
            }
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        throw new ResourceNotFoundException(functionPath, "Person not found");
    }
}