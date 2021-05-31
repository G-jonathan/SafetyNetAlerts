package com.openClassroomsProject.SafetyNetAlerts.controller;

import com.openClassroomsProject.SafetyNetAlerts.model.FireStation;
import com.openClassroomsProject.SafetyNetAlerts.service.IFireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fireStation")
public class FireStationController {
    @Autowired
    private IFireStationService fireStationService;

    @GetMapping
    public Iterable<FireStation> getFireStations() {
        return fireStationService.getFireStations();
    }

    @PostMapping
    public ResponseEntity<?> addStationAndAddressMapping(@Valid @RequestBody FireStation firestation) {
        try {
            fireStationService.addStationAndAddressMapping(firestation);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(firestation + "\n" + " --> has been successfully created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateFireStationNumberOfAnAddress(@Valid @RequestBody FireStation fireStation) {
        try {
            Optional<FireStation> fireStationUpdated = fireStationService.updateFireStationNumberOfAnAddress(fireStation);
            if (fireStationUpdated.isPresent()) {
                return new ResponseEntity<>(fireStationUpdated + "\n" + " --> has been successfully modified", HttpStatus.OK);
            }
            return new ResponseEntity<>(fireStationUpdated + "\n" + " --> fireStation not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/station/{stationNumber}")
    public ResponseEntity<?> deleteMappingOfAStation(@PathVariable("stationNumber") final String stationNumber) {
        try {
            Optional<List<FireStation>> fireStationsDeleted = fireStationService.deleteMappingOfAStation(stationNumber);
            if (fireStationsDeleted.isEmpty()) {
                return new ResponseEntity<>("Station n° " + stationNumber + " not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("stationNumber mapping n° " + stationNumber + "\n" + " ->> has been successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/address/{address}")
    public ResponseEntity<?> deleteMappingOfAnAddress(@PathVariable("address") final String address) {
        try {
            Optional<FireStation> fireStationDelete = fireStationService.deleteMappingOfAnAddress(address);
            if (fireStationDelete.isEmpty()) {
                return new ResponseEntity<>("Address " + address + " not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(fireStationDelete + "\n" + " ->> has been successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}