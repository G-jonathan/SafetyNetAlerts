package com.openClassroomsProject.SafetyNetAlerts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.openClassroomsProject.SafetyNetAlerts.model.FireStation;
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