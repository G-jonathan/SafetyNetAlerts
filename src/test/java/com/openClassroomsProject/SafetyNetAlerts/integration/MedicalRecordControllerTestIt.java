package com.openClassroomsProject.SafetyNetAlerts.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.service.IMedicalRecordService;
import com.openClassroomsProject.SafetyNetAlerts.service.starter.JsonDataService;
import org.hamcrest.Matchers;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MedicalRecordControllerTestIt {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void TestGetMedicalRecordsAndResponseIsOk() throws Exception {
        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(23)));
    }

    @Test
    @Order(2)
    void testAddMedicalRecordAndResponseIsOk() throws Exception {
        String bodyContentAddPerson = "{\n" +
                "\"firstName\":\"medicalRecordFirstNameTest\",\n" +
                "\"lastName\":\"medicalRecordLastNameTest\",\n" +
                "\"address\":\"AddressTest\",\n" +
                "\"city\":\"cityTest\",\n" +
                "\"zip\":\"11111\",\n" +
                "\"phone\":\"111111111\",\n" +
                "\"email\":\"emailTest.com\"\n" +
                "}";
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContentAddPerson))
                .andExpect(status().isCreated());
        String bodyContentAddMedicalRecord = "{ \"firstName\":\"medicalRecordFirstNameTest\", \"lastName\":\"medicalRecordLastNameTest\", \"birthdate\":\"01/03/1989\", \"medications\":[\"medicationTest1:111mg\", \"medicationTest2:222mg\"], \"allergies\":[\"allergiesTest1\"] }";
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContentAddMedicalRecord))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/personInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstName", "medicalRecordFirstNameTest")
                .param("lastName", "medicalRecordLastNameTest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is("medicalRecordFirstNameTest")))
                .andExpect(jsonPath("lastName", is("medicalRecordLastNameTest")))
                .andExpect(jsonPath("address", is("AddressTest")))
                .andExpect(jsonPath("medications[0]", is("medicationTest1:111mg")))
                .andExpect(jsonPath("medications[1]", is("medicationTest2:222mg")))
                .andExpect(jsonPath("allergies[0]", is("allergiesTest1")));
    }

    @Test
    @Order(3)
    void testAddMedicalRecordAndResponseIsConflict() throws Exception {
        String bodyContentAddMedicalRecord = "{ \"firstName\":\"medicalRecordFirstNameTest\", \"lastName\":\"medicalRecordLastNameTest\", \"birthdate\":\"01/03/1989\", \"medications\":[\"medicationTest1:111mg\", \"medicationTest2:222mg\"], \"allergies\":[\"allergiesTest1\"] }";
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContentAddMedicalRecord))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(4)
    void testUpdateAnExistingMedicalRecordAndResponseIsOk() throws Exception {
        String bodyContent = "{ \"firstName\":\"tEric\", \"lastName\":\"tCadigan\", \"birthdate\":\"08/06/1945\", \"medications\":[\"tradoxidine:400mg\"], \"allergies\":[] }";
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void testUpdateAnExistingMedicalRecordAndResponseIsNotFound() throws Exception {
        String bodyContent = "{ \"firstName\":\"firstnameTest\", \"lastName\":\"lastnameTest\", \"birthdate\":\"01/01/0001\", \"medications\":[\"medicationTest1:111mg\", \"medicationTest2:222mg\"], \"allergies\":[\"allergiesTest1\"] }";
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void testDeleteAMedicalRecordAndResponseIsOk() throws Exception {
        String bodyContent = "{ \"firstName\":\"tEric\", \"lastName\":\"tCadigan\" }";
        mockMvc.perform(delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAMedicalRecordAndResponseIsNotFound() throws Exception {
        String bodyContent = "{ \"firstName\":\"firstnameTest\", \"lastName\":\"lastnameTest\" }";
        mockMvc.perform(delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }
}