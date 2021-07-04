package com.openClassroomsProject.SafetyNetAlerts.controller;

import com.openClassroomsProject.SafetyNetAlerts.exception.CustomGenericException;
import com.openClassroomsProject.SafetyNetAlerts.exception.ResourceNotFoundException;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.ChildrenAndOtherMembers;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonInformation;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.service.IPersonService;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "Retrieve a list of children living at this address. include children's name, age and a list of other household members")
    @GetMapping("/childAlert")
    public ResponseEntity<ChildrenAndOtherMembers> getListOfChildrenLivingAtThisAddress(@RequestParam String address) {
        String functionPath = CLASSPATH + ".getListOfChildrenLivingAtThisAddress";
        log.info("Request received in " + functionPath);
        Optional<ChildrenAndOtherMembers> childrenAndOtherMembersOptional;
        try {
            childrenAndOtherMembersOptional = personService.getListOfChildrenLivingAtThisAddress(address);
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        if (childrenAndOtherMembersOptional.isEmpty()) {
            throw new ResourceNotFoundException(functionPath, "Nothing found for this address");
        }
        log.info("Request success in " + functionPath);
        ChildrenAndOtherMembers childrenAndOtherMembers = childrenAndOtherMembersOptional.get();
        return new ResponseEntity<>(childrenAndOtherMembers, HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve information about a person. Include name, address, age, mail, and medical history.")
    @GetMapping("/personInfo")
    public ResponseEntity<PersonInformation> getPersonInformation(@RequestParam String firstName, @RequestParam String lastName) {
        String functionPath = CLASSPATH + ".getPersonInformation";
        log.info("Request received in " + functionPath);
        Optional<PersonInformation> personInformation;
        try {
            personInformation = personService.getPersonInformation(firstName, lastName);
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        if (personInformation.isEmpty()) {
            throw new ResourceNotFoundException(functionPath, "Nothing found for this person");
        }
        log.info("Request success in " + functionPath);
        PersonInformation requestContent = personInformation.get();
        return new ResponseEntity<>(requestContent, HttpStatus.OK);
    }

    @ApiOperation(value = " Retrieve the email of all the people living in the city.")
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

    @ApiOperation(value = "Retrieve a list of all persons.")
    @GetMapping("/person")
    public ArrayList<Person> getPersons() {
        String functionPath = CLASSPATH + ".getPersons";
        log.info("Request received in " + functionPath);
        ArrayList<Person> requestContent;
        try {
            requestContent = personService.getPersons();
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        log.info("Request success in " + functionPath);
        return requestContent;
    }

    @ApiOperation(value = "Add a new person.")
    @PostMapping("/person")
    public ResponseEntity<?> addNewPerson(@Valid @RequestBody Person person) {
        String functionPath = CLASSPATH + ".addNewPerson";
        log.info("Request received in " + functionPath);
        try {
            Optional<Person> personCreated = personService.addNewPerson(person);
            if (personCreated.isPresent()) {
                log.info("Request success in " + functionPath +" -> Person successfully added: " + personCreated);
                return new ResponseEntity<>(personCreated + "\n" + " --> has been successfully modified", HttpStatus.CREATED);
            }
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        throw new ResourceNotFoundException(functionPath, "Person Already exist");
    }

    @ApiOperation(value = "Update an existing person.")
    @PutMapping("/person")
    public ResponseEntity<?> updateAnExistingPerson(@Valid @RequestBody Person person) {
        String functionPath = CLASSPATH + ".updateAnExistingPerson";
        log.info("Request received in " + functionPath);
        try {
            Optional<Person> personUpdated = personService.updateAnExistingPerson(person);
            if (personUpdated.isPresent()) {
                log.info("Request success in " + functionPath +" -> Person successfully modified: " + personUpdated);
                return new ResponseEntity<>(personUpdated + "\n" + " --> has been successfully modified", HttpStatus.OK);
            }
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        throw new ResourceNotFoundException(functionPath, "Person not found");
    }

    @ApiOperation(value = "Delete a person.")
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