package com.openClassroomsProject.SafetyNetAlerts.controller;

import com.openClassroomsProject.SafetyNetAlerts.model.Person;
import com.openClassroomsProject.SafetyNetAlerts.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private IPersonService personService;

    @GetMapping
    public Iterable<Person> getPersons() {
        return personService.getPersons();
    }

    @PostMapping
    public ResponseEntity<?> addNewPerson(@Valid @RequestBody Person person) {
        try {
            personService.addNewPerson(person);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(person + "\n" + " --> has been successfully created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateAnExistingPerson(@Valid @RequestBody Person person) {
        try {
            Optional<Person> personUpdated = personService.updateAnExistingPerson(person);
            if (personUpdated.isPresent()) {
                return new ResponseEntity<>(personUpdated + "\n" + " --> has been successfully modified", HttpStatus.OK);
            }
            return new ResponseEntity<>(personUpdated + "\n" + " --> person not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAPerson(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        try {
            boolean isSuccess = personService.deleteAPerson(firstName, lastName);
            if (isSuccess) {
                return new ResponseEntity<>(firstName + " " + lastName + " person has been successfully deleted", HttpStatus.OK);
            }
            return new ResponseEntity<>(firstName + " " + lastName + " person not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}