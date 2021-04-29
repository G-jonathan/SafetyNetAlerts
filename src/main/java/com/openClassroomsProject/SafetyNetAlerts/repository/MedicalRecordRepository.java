package com.openClassroomsProject.SafetyNetAlerts.repository;

import com.openClassroomsProject.SafetyNetAlerts.model.MedicalRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepository extends CrudRepository<MedicalRecord, Long> {
}