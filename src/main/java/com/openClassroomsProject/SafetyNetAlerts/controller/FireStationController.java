package com.openClassroomsProject.SafetyNetAlerts.controller;

import com.openClassroomsProject.SafetyNetAlerts.exception.CustomGenericException;
import com.openClassroomsProject.SafetyNetAlerts.exception.ResourceNotFoundException;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.HouseHold;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonAndFireStationNumberWhoServedHim;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.FireStation;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonListCoveredByAFireStation;
import com.openClassroomsProject.SafetyNetAlerts.service.IFireStationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class FireStationController {
    @Autowired
    private IFireStationService fireStationService;
    private static final String CLASSPATH = "com.openClassroomsProject.SafetyNetAlerts.controller.FireStationController";

    @GetMapping("/flood/stations")
    public ResponseEntity<ArrayList<HouseHold>> getListOfHomesServedByThisStations(@RequestParam ArrayList<String> stations) {
        String functionPath = CLASSPATH + ".getListOfHomesServedByThisStations";
        log.info("Request received  in " + functionPath);
        ArrayList<HouseHold> requestContent;
        try {
            requestContent = fireStationService.getListOfHomesServedByThisStations(stations);
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        if (requestContent.isEmpty()) {
            throw new ResourceNotFoundException(functionPath, "Nothing found for this station");
        }
        log.info("Request success in " + functionPath);
        return new ResponseEntity<>(requestContent, HttpStatus.OK);
    }

    @GetMapping("fire")
    public ResponseEntity<ArrayList<PersonAndFireStationNumberWhoServedHim>> getPersonListAndHerFireStationNumber(@RequestParam String address) {
        String functionPath = CLASSPATH + ".PersonAndFireStationNumberWhoServedHim";
        log.info("Request received  in " + functionPath);
        ArrayList<PersonAndFireStationNumberWhoServedHim> requestContent;
        try {
            requestContent = fireStationService.getPersonListAndHerFireStationNumber(address);
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        if (requestContent.isEmpty()) {
            throw new ResourceNotFoundException(functionPath, "Nothing found for this address");
        }
        log.info("Request success in " + functionPath);
        return new ResponseEntity<>(requestContent, HttpStatus.OK);
    }

    @GetMapping("/firestation")
    public ResponseEntity<Optional<PersonListCoveredByAFireStation>> getPersonListCoveredByAFireStation(@RequestParam String fireStation) {
        String functionPath = CLASSPATH + ".getPersonListCoveredByAFireStation";
        log.info("Request received  in " + functionPath);
        Optional<PersonListCoveredByAFireStation> requestContent;
        try {
            requestContent = fireStationService.getPersonListCoveredByAFireStation(fireStation);
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        if (requestContent.isEmpty()) {
            throw new ResourceNotFoundException(functionPath, "Nothing found for this fireStation");
        }
        log.info("Request success in " + functionPath);
        return new ResponseEntity<>(requestContent, HttpStatus.OK);
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<ArrayList<String>> getPhoneNumbersPersonServedByAFireStation(@RequestParam String fireStation) {
        String functionPath = CLASSPATH + ".getPhoneNumbersPersonServedByAFireStation";
        log.info("Request received  in " + functionPath);
        ArrayList<String> requestContent;
        try {
            requestContent = fireStationService.getPhoneNumbersPersonServedByAFireStation(fireStation);
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        if (requestContent.isEmpty()) {
            throw new ResourceNotFoundException(functionPath, "Nothing found for this fireStation");
        }
        log.info("Request success in " + functionPath);
        return new ResponseEntity<>(requestContent, HttpStatus.OK);
    }

    @GetMapping("/fireStation")
    public Iterable<FireStation> getFireStations() {
        String functionPath = CLASSPATH + ".getFireStations";
        log.info("Request received  in " + functionPath);
        Iterable<FireStation> requestContent;
        try {
            requestContent = fireStationService.getFireStations();
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        log.info("Request success in " + functionPath);
        return requestContent;
    }

    @PostMapping("/fireStation")
    public ResponseEntity<?> addStationAndAddressMapping(@Valid @RequestBody FireStation firestation) {
        String functionPath = CLASSPATH + ".addStationAndAddressMapping";
        log.info("Request received  in " + functionPath);
        try {
            fireStationService.addStationAndAddressMapping(firestation);
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        log.info("Request success in " + functionPath);
        return new ResponseEntity<>(firestation + "\n" + " --> has been successfully created", HttpStatus.CREATED);
    }

    @PutMapping("/fireStation")
    public ResponseEntity<?> updateFireStationNumberOfAnAddress(@Valid @RequestBody FireStation fireStation) {
        String functionPath = CLASSPATH + ".updateFireStationNumberOfAnAddress";
        log.info("Request received in " + functionPath);
        try {
            Optional<FireStation> fireStationUpdated = fireStationService.updateFireStationNumberOfAnAddress(fireStation);
            if (fireStationUpdated.isPresent()) {
                log.info("Request success in: " + functionPath + " FireStation address number successfully modified");
                return new ResponseEntity<>(fireStationUpdated + "\n" + " --> has been successfully modified", HttpStatus.OK);
            }
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        throw new ResourceNotFoundException(functionPath, "FireStation not found");
    }

    @DeleteMapping("/fireStation/station/{stationNumber}")
    public ResponseEntity<?> deleteMappingOfAStation(@PathVariable("stationNumber") final String stationNumber) {
        String functionPath = CLASSPATH + ".deleteMappingOfAStation";
        log.info("Request received  in " + functionPath);
        try {
            Optional<List<FireStation>> fireStationsDeleted = fireStationService.deleteMappingOfAStation(stationNumber);
            if (fireStationsDeleted.isPresent()) {
                log.info("Request success in " + functionPath + ". " + fireStationsDeleted + " fireStation deleted: ");
                return new ResponseEntity<>("stationNumber mapping n° " + stationNumber + "\n" + " ->> has been successfully deleted", HttpStatus.OK);
            }
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        throw new ResourceNotFoundException(functionPath, "Station number not found");
    }

    @DeleteMapping("/fireStation/address/{address}")
    public ResponseEntity<?> deleteMappingOfAnAddress(@PathVariable("address") final String address) {
        String functionPath = CLASSPATH + ".deleteMappingOfAnAddress";
        log.info("Request received  in " + functionPath);
        try {
            Optional<FireStation> fireStationDelete = fireStationService.deleteMappingOfAnAddress(address);
            if (fireStationDelete.isPresent()) {
                log.info("Request success in " + functionPath + ". " + fireStationDelete + " fireStation deleted: ");
                return new ResponseEntity<>(fireStationDelete + "\n" + " ->> has been successfully deleted", HttpStatus.OK);
            }
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        throw new ResourceNotFoundException(functionPath, "Address not found");
    }
}