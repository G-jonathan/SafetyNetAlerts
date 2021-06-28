package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.HouseHold;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonAndFireStationNumberWhoServedHim;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.FireStation;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonListCoveredByAFireStation;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public interface IFireStationService {

     Iterable<FireStation> getFireStations();

     void addStationAndAddressMapping(FireStation firestation);

     Optional<FireStation> updateFireStationNumberOfAnAddress(FireStation fireStation);

     Optional<List<FireStation>> deleteMappingOfAStation(String stationId);

     Optional<FireStation> deleteMappingOfAnAddress(String address);

     ArrayList<String> getPhoneNumbersPersonServedByAFireStation(String fireStationNumber);

     Optional<PersonListCoveredByAFireStation> getPersonListCoveredByAFireStation(String fireStationNumber);

     ArrayList<PersonAndFireStationNumberWhoServedHim> getPersonListAndHerFireStationNumber(String Address);

     ArrayList<HouseHold> getListOfHomesServedByThisStations(ArrayList<String> stations);
}