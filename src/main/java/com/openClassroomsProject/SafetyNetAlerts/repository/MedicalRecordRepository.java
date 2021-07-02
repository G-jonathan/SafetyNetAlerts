package com.openClassroomsProject.SafetyNetAlerts.repository;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends CrudRepository<MedicalRecord, Long> {

    ArrayList<MedicalRecord> findAll();
    Optional<MedicalRecord> findMedicalRecordByFirstNameAndLastName(String firstName, String lastName);
}