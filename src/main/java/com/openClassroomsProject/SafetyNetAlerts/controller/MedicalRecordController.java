package com.openClassroomsProject.SafetyNetAlerts.controller;

import com.openClassroomsProject.SafetyNetAlerts.model.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.service.IMedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
    @Autowired
    private IMedicalRecordService medicalRecordService;

    @GetMapping
    public Iterable<MedicalRecord> getMedicalRecords() {
        return medicalRecordService.getMedicalRecords();
    }

    @PostMapping
    public ResponseEntity<?> addMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        try {
            medicalRecordService.addMedicalRecord(medicalRecord);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(medicalRecord + "\n" + " --> has been successfully created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateAnExistingMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        try {
            Optional<MedicalRecord> medicalRecordUpdated = medicalRecordService.updateAnExistingMedicalRecord(medicalRecord);
            if (medicalRecordUpdated.isPresent()) {
                return new ResponseEntity<>(medicalRecordUpdated + "\n" + " --> has been successfully modified", HttpStatus.OK);
            }
            return new ResponseEntity<>(medicalRecordUpdated + "\n" + " --> medical record not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAMedicalRecord(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        try {
            boolean isSuccess = medicalRecordService.deleteAMedicalRecord(firstName, lastName);
            if (isSuccess) {
                return new ResponseEntity<>(firstName + " " + lastName + " medical record has been successfully deleted", HttpStatus.OK);
            }
            return new ResponseEntity<>(firstName + " " + lastName + " medical record not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}