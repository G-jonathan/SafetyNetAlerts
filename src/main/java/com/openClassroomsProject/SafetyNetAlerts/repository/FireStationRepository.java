package com.openClassroomsProject.SafetyNetAlerts.repository;

import com.openClassroomsProject.SafetyNetAlerts.model.FireStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FireStationRepository extends JpaRepository<FireStation, Long> {

    Optional<List<FireStation>> findByStation(String stationNumber);

    Optional<FireStation> findByAddress(String address);
}