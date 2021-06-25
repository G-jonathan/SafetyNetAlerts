package com.openClassroomsProject.SafetyNetAlerts.controller;

import com.openClassroomsProject.SafetyNetAlerts.exception.CustomGenericException;
import com.openClassroomsProject.SafetyNetAlerts.exception.ResourceNotFoundException;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.service.IMedicalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
    @Autowired
    private IMedicalRecordService medicalRecordService;
    private static final String CLASSPATH = "com.openClassroomsProject.SafetyNetAlerts.controller.MedicalRecordController";

    @GetMapping
    public Iterable<MedicalRecord> getMedicalRecords() {
        String functionPath = CLASSPATH + ".getMedicalRecords";
        log.info("Request received  in " + functionPath);
        Iterable<MedicalRecord> requestContent;
        try {
            requestContent = medicalRecordService.getMedicalRecords();
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        log.info("Request success in " + functionPath);
        return requestContent;
    }

    @PostMapping
    public ResponseEntity<?> addMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        String functionPath = CLASSPATH + ".addMedicalRecord";
        log.info("Request received  in " + functionPath);
        try {
            medicalRecordService.addMedicalRecord(medicalRecord);
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        log.info("Request success in " + functionPath);
        return new ResponseEntity<>(medicalRecord + "\n" + " --> has been successfully created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateAnExistingMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        String functionPath = CLASSPATH + ".updateAnExistingMedicalRecord";
        log.info("Request received in " + functionPath);
        try {
            Optional<MedicalRecord> medicalRecordUpdated = medicalRecordService.updateAnExistingMedicalRecord(medicalRecord);
            if (medicalRecordUpdated.isPresent()) {
                log.info("Request success in: " + functionPath + " Medical record successfully modified");
                return new ResponseEntity<>(medicalRecord + "\n" + " --> has been successfully modified", HttpStatus.OK);
            }
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        throw new ResourceNotFoundException(functionPath, "Medical record not found");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAMedicalRecord(@Valid @RequestBody UniqueIdentifier uniqueIdentifier) {
        String functionPath = CLASSPATH + ".deleteAMedicalRecord";
        log.info("Request received  in " + functionPath);
        try {
            boolean isSuccess = medicalRecordService.deleteAMedicalRecord(uniqueIdentifier);
            if (isSuccess) {
                log.info("Request success in " + functionPath + ". " + uniqueIdentifier + "'s Medical record deleted: ");
                return new ResponseEntity<>(uniqueIdentifier + "'s medical record has been successfully deleted", HttpStatus.OK);
            }
        } catch (Exception exception) {
            throw new CustomGenericException(functionPath, exception);
        }
        throw new ResourceNotFoundException(functionPath, "Medical record not found");
    }
}