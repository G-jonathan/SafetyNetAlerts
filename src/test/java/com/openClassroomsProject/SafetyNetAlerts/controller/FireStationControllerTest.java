package com.openClassroomsProject.SafetyNetAlerts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.openClassroomsProject.SafetyNetAlerts.TestDataSourceConfigImpl;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.FireStation;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.HouseHold;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonAndFireStationNumberWhoServedHim;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonCoveredByAFireStation;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonListCoveredByAFireStation;
import com.openClassroomsProject.SafetyNetAlerts.service.IFireStationService;
import com.openClassroomsProject.SafetyNetAlerts.service.starter.JsonDataService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.*;

@Import(TestDataSourceConfigImpl.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = FireStationController.class)
public class FireStationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JsonDataService jsonDataService;
    @MockBean
    private IFireStationService fireStationService;

    @Test
    public void testGetListOfHomesServedByThisStationsAndResponseIsOk() throws Exception {
        ArrayList<HouseHold> houseHoldArrayListTest = new ArrayList<>(Collections.singletonList(new HouseHold("houseTest", new ArrayList<>())));
        ArrayList<String> stringListTest = new ArrayList<>(Arrays.asList("test", "test"));
        when(fireStationService.getListOfHomesServedByThisStations(stringListTest)).thenReturn(houseHoldArrayListTest);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addAll("stations", stringListTest);
        mockMvc.perform(get("/flood/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetListOfHomesServedByThisStationsAndResponseIsNotFound() throws Exception {
        when(fireStationService.getListOfHomesServedByThisStations(any(ArrayList.class))).thenReturn(new ArrayList<>());
        mockMvc.perform(get("//flood/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("stations", "00", "99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetPersonListAndHerFireStationNumberAndResponseIsOk() throws Exception {
        PersonAndFireStationNumberWhoServedHim personAndFireStationNumberWhoServedHim = new PersonAndFireStationNumberWhoServedHim();
        ArrayList<PersonAndFireStationNumberWhoServedHim> personAndFireStationNumberWhoServedHimArrayListTest = new ArrayList<>();
        personAndFireStationNumberWhoServedHimArrayListTest.add(personAndFireStationNumberWhoServedHim);
        when(fireStationService.getPersonListAndHerFireStationNumber(any(String.class))).thenReturn(personAndFireStationNumberWhoServedHimArrayListTest);
        mockMvc.perform(get("/fire")
                .contentType(MediaType.APPLICATION_JSON)
                .param("address", "addressTest"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPersonListAndHerFireStationNumberAndResponseIsNotFound() throws Exception {
        when(fireStationService.getPersonListAndHerFireStationNumber(any(String.class))).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/fire")
                .contentType(MediaType.APPLICATION_JSON)
                .param("address", "addressTest"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetPersonListCoveredByAFireStationAndResponseIsOk() throws Exception {
        ArrayList<PersonCoveredByAFireStation> testList = new ArrayList<>();
        HashMap<String, String> testMap = new HashMap<>();
        PersonListCoveredByAFireStation testObject = new PersonListCoveredByAFireStation(testList, testMap);

        when(fireStationService.getPersonListCoveredByAFireStation(any(String.class))).thenReturn(Optional.of(testObject));
        mockMvc.perform(get("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fireStation", "0"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPersonListCoveredByAFireStationAndResponseIsNotFound() throws Exception {
        when(fireStationService.getPersonListCoveredByAFireStation(any(String.class))).thenReturn(Optional.empty());
        mockMvc.perform(get("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fireStation", "0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetPhoneNumbersPersonServedByAFireStationAndResponseIsOk() throws Exception {
        ArrayList<String> testList = new ArrayList<>(Arrays.asList("000-000-000", "999-999-999"));
        when(fireStationService.getPhoneNumbersPersonServedByAFireStation(any(String.class))).thenReturn(testList);
        mockMvc.perform(get("/phoneAlert")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fireStation", "0"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPhoneNumbersPersonServedByAFireStationAndResponseIsNotFound() throws Exception {
        when(fireStationService.getPhoneNumbersPersonServedByAFireStation(any(String.class))).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/phoneAlert")
                .contentType(MediaType.APPLICATION_JSON)
                .param("fireStation", "0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetFireStationsAndResponseIsOk() throws Exception {
        mockMvc.perform(get("/fireStation"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddStationAndAddressMappingAndResponseIsCreated() throws Exception {
        FireStation fireStationTest = new FireStation();
        String bodyContent = "{\"address\": \"TestAddress\", \"station\": \"00\"}";
        when(fireStationService.addStationAndAddressMapping(any(FireStation.class))).thenReturn(Optional.of(fireStationTest));
        mockMvc.perform(post("/fireStation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddStationAndAddressMappingAndResponseIsNotFoundBecauseAlreadyExist() throws Exception {
        String bodyContent = "{\"address\": \"TestAddress\", \"station\": \"00\"}";
        when(fireStationService.addStationAndAddressMapping(any(FireStation.class))).thenReturn(Optional.empty());
        mockMvc.perform(post("/fireStation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateFireStationNumberOfAnAddressAndResponseIsOk() throws Exception {
        FireStation fireStationTest = new FireStation();
        String bodyContent = "{\"address\": \"TestAddress\", \"station\": \"00\"}";
        when(fireStationService.updateFireStationNumberOfAnAddress(any(FireStation.class))).thenReturn(Optional.of(fireStationTest));
        mockMvc.perform(put("/fireStation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateFireStationNumberOfAnAddressAndResponseIsNotFound() throws Exception {
        String bodyContent = "{\"address\": \"TestAddress\", \"station\": \"00\"}";
        when(fireStationService.updateFireStationNumberOfAnAddress(any(FireStation.class))).thenReturn(Optional.empty());
        mockMvc.perform(put("/fireStation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMappingOfAStationAndResponseIsOk() throws Exception {
        ArrayList<FireStation> fireStationListTest = new ArrayList<>();
        when(fireStationService.deleteMappingOfAStation(any(String.class))).thenReturn(Optional.of(fireStationListTest));
        mockMvc.perform(delete("/fireStation/station/00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMappingOfAStationAndResponseIsNotFound() throws Exception {
        when(fireStationService.deleteMappingOfAStation(any(String.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/fireStation/station/00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMappingOfAnAddressAndResponseIsOk() throws Exception {
        FireStation fireStationTest = new FireStation();
        when(fireStationService.deleteMappingOfAnAddress(any(String.class))).thenReturn(Optional.of(fireStationTest));
        mockMvc.perform(delete("/fireStation/address/AddressTest"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMappingOfAnAddressAndResponseIsNotFound() throws Exception {
        when(fireStationService.deleteMappingOfAnAddress(any(String.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/fireStation/address/AddressTest")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}