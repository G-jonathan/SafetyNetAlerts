package com.openClassroomsProject.SafetyNetAlerts.controller;

import com.openClassroomsProject.SafetyNetAlerts.model.FireStation;
import com.openClassroomsProject.SafetyNetAlerts.service.impl.FireStationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fireStation")
public class FireStationController {

    @Autowired
    private FireStationServiceImpl fireStationService;

    @GetMapping
    public Iterable<FireStation> getFireStations() {
        return fireStationService.getFireStations();
    }

    @PostMapping
    public ResponseEntity<?> addBarracksAndAddressMapping(@RequestBody FireStation firestation) {
        if (firestation.getAddress().isEmpty() || firestation.getStation().isEmpty()) {
            return new ResponseEntity<>("A string address and a string station number is required", HttpStatus.BAD_REQUEST);
        }
        try {
            fireStationService.addStationAndAddressMapping(firestation);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{address}")
    public ResponseEntity<?> updateFireStationNumberToAnAddress(@PathVariable("address") final String address, @RequestBody String newFireStationNumber) {
        try {
           boolean response = fireStationService.updateFireStationNumberToAnAddress(address, newFireStationNumber);
           if (response) {
               return new ResponseEntity<>(HttpStatus.OK);
           }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/station/{stationNumber}")
    public ResponseEntity<?> deleteMappingOfAStation(@PathVariable("stationNumber") final String stationNumber) {
        try {
            Optional<List<FireStation>> fireStationsDeleted = fireStationService.deleteMappingOfAStation(stationNumber);
            if (fireStationsDeleted.isEmpty()) {
                return new ResponseEntity<>("Station n° " + stationNumber + " not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(fireStationsDeleted, HttpStatus.OK);
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
            return new ResponseEntity<>(fireStationDelete, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
