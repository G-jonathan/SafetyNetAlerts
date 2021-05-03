package com.openClassroomsProject.SafetyNetAlerts.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.openClassroomsProject.SafetyNetAlerts.controller.FireStationController;
import com.openClassroomsProject.SafetyNetAlerts.service.FireStationService;
import com.openClassroomsProject.SafetyNetAlerts.service.JsonDataService;
import com.openClassroomsProject.SafetyNetAlerts.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FireStationController.class)
public class FireStationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JsonDataService jsonDataService;
    @MockBean
    private FireStationService fireStationService;

    @Test
    public void testGetFireStations() throws Exception {
        mockMvc.perform(get("/fireStation"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddStationAndAddressMapping() throws Exception {
        mockMvc.perform(post("/fireStation"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateFireStationNumberToAnAddress() throws Exception {
        mockMvc.perform(patch("/fireStation/{address}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMappingOfAStation() throws Exception {
        mockMvc.perform(delete("/fireStation/station/{stationNumber}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMappingOfAnAddress() throws Exception {
        mockMvc.perform(delete("/fireStation/address/{address}"))
                .andExpect(status().isOk());
    }
}
