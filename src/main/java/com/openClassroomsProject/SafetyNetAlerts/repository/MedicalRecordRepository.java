package com.openClassroomsProject.SafetyNetAlerts.repository;

import com.openClassroomsProject.SafetyNetAlerts.model.MedicalRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends CrudRepository<MedicalRecord, Long> {

    Optional<MedicalRecord> findMedicalRecordByFirstNameAndLastName(String firstName, String lastName);
}