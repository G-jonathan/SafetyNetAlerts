package com.openClassroomsProject.SafetyNetAlerts.service.impl;

import com.openClassroomsProject.SafetyNetAlerts.model.FireStation;
import com.openClassroomsProject.SafetyNetAlerts.repository.FireStationRepository;
import com.openClassroomsProject.SafetyNetAlerts.service.IFireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class FireStationServiceImpl implements IFireStationService {
    @Autowired
    private FireStationRepository fireStationRepository;

    @Override
    public Iterable<FireStation> getFireStations() {
        return fireStationRepository.findAll();
    }

    @Override
    public void addStationAndAddressMapping(FireStation fireStation) {
        try {
            fireStationRepository.save(fireStation);
        } catch (Exception e) {
            System.out.println("Error attempting to add data in [addStationAndAddressMapping] method");
        }
    }

    @Override
    public Optional<FireStation> updateFireStationNumberOfAnAddress(FireStation fireStation) {
        try {
            Optional<FireStation> fireStationToUpdate = fireStationRepository.findByAddress(fireStation.getAddress());
            if (fireStationToUpdate.isPresent()) {
                FireStation fireStationUpdated = fireStationToUpdate.get();
                fireStationUpdated.setStation(fireStation.getStation());
                fireStationRepository.save(fireStationUpdated);
                return Optional.of(fireStationUpdated);
            }
        } catch (Exception e) {
            System.out.println("Error attempting to update data in [updateFireStationNumberToAnAddress] method");
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<FireStation>> deleteMappingOfAStation(String stationNumber) {
        Optional<List<FireStation>> fireStationsToDelete = fireStationRepository.findByStation(stationNumber);
        if (fireStationsToDelete.isPresent()) {
            try {
                List<FireStation> _fireStationsToDelete = fireStationsToDelete.get();
                for (FireStation currentFireStation : _fireStationsToDelete) {
                    fireStationRepository.delete(currentFireStation);
                }
            } catch (Exception e) {
                System.out.println("Error attempting to delete data in [deleteMappingOfAStation] method");
            }
        }
        return fireStationsToDelete;
    }

    @Override
    public Optional<FireStation> deleteMappingOfAnAddress(String address) {
        Optional<FireStation> fireStationToDelete = fireStationRepository.findByAddress(address);
        if (fireStationToDelete.isPresent()) {
            try {
                FireStation _fireStationToDelete = fireStationToDelete.get();
                fireStationRepository.delete(_fireStationToDelete);
            } catch (Exception e) {
                System.out.println("Error attempting to delete data in [deleteMappingOfAnAddress] method");
            }
        }
        return fireStationToDelete;
    }
}