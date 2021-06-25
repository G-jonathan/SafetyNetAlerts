package com.openClassroomsProject.SafetyNetAlerts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openClassroomsProject.SafetyNetAlerts.model.PersonAndFireStationNumberWhoServedHim;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.FireStation;
import com.openClassroomsProject.SafetyNetAlerts.model.PersonCoveredByAFireStation;
import com.openClassroomsProject.SafetyNetAlerts.model.PersonListCoveredByAFireStation;
import com.openClassroomsProject.SafetyNetAlerts.service.IFireStationService;
import com.openClassroomsProject.SafetyNetAlerts.service.JsonDataService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

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
        String bodyContent = "{\"address\": \"TestAddress\", \"station\": \"00\"}";
        mockMvc.perform(post("/fireStation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isCreated());
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