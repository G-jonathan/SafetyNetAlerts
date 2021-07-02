package com.openClassroomsProject.SafetyNetAlerts.controller;

import com.openClassroomsProject.SafetyNetAlerts.TestDataSourceConfigImpl;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.service.IMedicalRecordService;
import com.openClassroomsProject.SafetyNetAlerts.service.starter.JsonDataService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;

@Import(TestDataSourceConfigImpl.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MedicalRecordController.class)
class MedicalRecordControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JsonDataService jsonDataService;
    @MockBean
    private IMedicalRecordService medicalRecordService;

    @Test
    void TestGetMedicalRecordsAndResponseIsOk() throws Exception {
        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddMedicalRecordAndResponseIsOk() throws Exception {
        String bodyContent = "{ \"firstName\":\"firstNameTest\", \"lastName\":\"lastnameTest\", \"birthdate\":\"01/01/0001\", \"medications\":[\"medicationTest1:111mg\", \"medicationTest2:222mg\"], \"allergies\":[\"allergiesTest1\"] }";
        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(true);
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddMedicalRecordAndResponseIsConflict() throws Exception {
        String bodyContent = "{ \"firstName\":\"firstNameTest\", \"lastName\":\"lastnameTest\", \"birthdate\":\"01/01/0001\", \"medications\":[\"medicationTest1:111mg\", \"medicationTest2:222mg\"], \"allergies\":[\"allergiesTest1\"] }";
        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(false);
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateAnExistingMedicalRecordAndResponseIsOk() throws Exception {
        MedicalRecord medicalRecordTest = new MedicalRecord();
        medicalRecordTest.setFirstName("firstNameTest");
        medicalRecordTest.setLastName("lastNameTest");
        medicalRecordTest.setBirthdate("00/00/0000");
        medicalRecordTest.setMedications(List.of("medicationsTest1:999mg", "medicationsTest2:000mg"));
        medicalRecordTest.setAllergies(List.of("allergiesTest1"));
        String bodyContent = "{ \"firstName\":\"firstnameTest\", \"lastName\":\"lastnameTest\", \"birthdate\":\"01/01/0001\", \"medications\":[\"medicationTest1:111mg\", \"medicationTest2:222mg\"], \"allergies\":[\"allergiesTest1\"] }";
        when(medicalRecordService.updateAnExistingMedicalRecord(any(MedicalRecord.class))).thenReturn(Optional.of(medicalRecordTest));
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateAnExistingMedicalRecordAndResponseIsNotFound() throws Exception {
        String bodyContent = "{ \"firstName\":\"firstnameTest\", \"lastName\":\"lastnameTest\", \"birthdate\":\"01/01/0001\", \"medications\":[\"medicationTest1:111mg\", \"medicationTest2:222mg\"], \"allergies\":[\"allergiesTest1\"] }";
        when(medicalRecordService.updateAnExistingMedicalRecord(any(MedicalRecord.class))).thenReturn(Optional.empty());
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAMedicalRecordAndResponseIsOk() throws Exception {
        String bodyContent = "{ \"firstName\":\"firstnameTest\", \"lastName\":\"lastnameTest\" }";
        when(medicalRecordService.deleteAMedicalRecord(any(UniqueIdentifier.class))).thenReturn(true);
        mockMvc.perform(delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAMedicalRecordAndResponseIsNotFound() throws Exception {
        String bodyContent = "{ \"firstName\":\"firstnameTest\", \"lastName\":\"lastnameTest\" }";
        when(medicalRecordService.deleteAMedicalRecord(any(UniqueIdentifier.class))).thenReturn(false);
        mockMvc.perform(delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }
}