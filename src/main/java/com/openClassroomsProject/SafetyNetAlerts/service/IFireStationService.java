package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.FireStation;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface IFireStationService {

     Iterable<FireStation> getFireStations();

     void addStationAndAddressMapping(FireStation firestation);

     Optional<FireStation> updateFireStationNumberOfAnAddress(FireStation fireStation);

     Optional<List<FireStation>> deleteMappingOfAStation(String stationId);

     Optional<FireStation> deleteMappingOfAnAddress(String address);
}