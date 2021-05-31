package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.FireStation;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface FireStationService {

     Iterable<FireStation> getFireStations();

     void addStationAndAddressMapping(FireStation firestation);

     boolean updateFireStationNumberToAnAddress(String address, String newFireStationNumber);

     Optional<List<FireStation>> deleteMappingOfAStation(String stationId);

     Optional<FireStation> deleteMappingOfAnAddress(String address);
}