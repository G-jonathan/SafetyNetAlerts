package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.*;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.FireStation;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public interface IFireStationService {

     ArrayList<FireStation> getFireStations();

     Optional<FireStation> addStationAndAddressMapping(FireStation firestation);

     Optional<FireStation> updateFireStationNumberOfAnAddress(FireStation fireStation);

     Optional<List<FireStation>> deleteMappingOfAStation(String stationId);

     Optional<FireStation> deleteMappingOfAnAddress(String address);

     ArrayList<String> getPhoneNumbersPersonServedByAFireStation(String fireStationNumber);

     Optional<PersonListCoveredByAFireStation> getPersonListCoveredByAFireStation(String fireStationNumber);

     HashMap<String, String> calculationOfTheNumberOfAdultsAndChildren(ArrayList<UniqueIdentifier> uniqueIdentifierArrayList);

     ArrayList<PersonAndFireStationNumberWhoServedHim> getPersonListAndHerFireStationNumber(String Address);

     ArrayList<HouseHold> getListOfHomesServedByThisStations(ArrayList<String> stations);

     ArrayList<HouseHoldMember> buildHouseHoldMemberArrayList(ArrayList<Person> personWhoLiveAtTheSameAddress);
}