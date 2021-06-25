package com.openClassroomsProject.SafetyNetAlerts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.service.IPersonService;
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
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JsonDataService jsonDataService;
    @MockBean
    private IPersonService personService;

    @Test
    void testGetEmailsOfCityDwellersAndResponseIsOk() throws Exception {
        ArrayList<String> requestContent = new ArrayList<String>(Arrays.asList("emailTest@test.fr","emailTest2@test.fr"));
        when(personService.getEmailsOfCityDwellers(any(String.class))).thenReturn(requestContent);
        mockMvc.perform(get("/communityEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .param("city", "cityTest"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetEmailsOfCityDwellersAndResponseIsNotFound() throws Exception {
        ArrayList<String> requestContent = new ArrayList<>();
        when(personService.getEmailsOfCityDwellers(any(String.class))).thenReturn(requestContent);
        mockMvc.perform(get("/communityEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .param("city", "cityTest"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPersonsAndResponseIsOk() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddNewPersonAndResponseIsCreated() throws Exception {
        String bodyContent = "{\n" +
                "\"firstName\":\"firstNameTest\",\n" +
                "\"lastName\":\"lastNameTest\",\n" +
                "\"address\":\"AddressTest\",\n" +
                "\"city\":\"cityTest\",\n" +
                "\"zip\":\"11111\",\n" +
                "\"phone\":\"111111111\",\n" +
                "\"email\":\"emailTest.com\"\n" +
                "}";
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateAnExistingPersonAndResponseIsOk() throws Exception {
        Person personTest = new Person();
        personTest.setFirstName("firstNameTestUpdateAnExistingPerson");
        personTest.setLastName("lastNameTestUpdateAnExistingPerson");
        personTest.setAddress("AddressTestUpdateAnExistingPerson");
        personTest.setCity("cityTestUpdateAnExistingPerson");
        personTest.setZip("00000");
        personTest.setPhone("000000000");
        personTest.setEmail("emailTestUpdateAnExistingPerson.com");
        String bodyContent = "{\n" +
                "\"firstName\":\"firstNameTestUpdateAnExistingPerson\",\n" +
                "\"lastName\":\"lastNameTestUpdateAnExistingPerson\",\n" +
                "\"address\":\"AddressTestUpdateAnExistingPerson\",\n" +
                "\"city\":\"cityTestUpdateAnExistingPerson\",\n" +
                "\"zip\":\"00023\",\n" +
                "\"phone\":\"000000023\",\n" +
                "\"email\":\"emailTestUpdateAnExistingPerson.com\"\n" +
                "}";
        when(personService.updateAnExistingPerson(any(Person.class))).thenReturn(Optional.of(personTest));
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateAnExistingPersonAndResponseIsNotFound() throws Exception {
        String bodyContent = "{\n" +
                "\"firstName\":\"firstNameTestUpdateAnExistingPerson\",\n" +
                "\"lastName\":\"lastNameTestUpdateAnExistingPerson\",\n" +
                "\"address\":\"AddressTestUpdateAnExistingPerson\",\n" +
                "\"city\":\"cityTestUpdateAnExistingPerson\",\n" +
                "\"zip\":\"00023\",\n" +
                "\"phone\":\"000000023\",\n" +
                "\"email\":\"emailTestUpdateAnExistingPerson.com\"\n" +
                "}";
        when(personService.updateAnExistingPerson(any(Person.class))).thenReturn(Optional.empty());
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAPersonAndResponseIsOk() throws Exception {
        when(personService.deleteAPerson(any(UniqueIdentifier.class))).thenReturn(true);
        mockMvc.perform(delete("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstName", "firstNameTest")
                .param("lastName", "lastNameTest"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAPersonAndResponseIsNotFound() throws Exception {
        when(personService.deleteAPerson(any(UniqueIdentifier.class))).thenReturn(false);
        mockMvc.perform(delete("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstName", "firstNameTest")
                .param("lastName", "lastNameTest"))
                .andExpect(status().isNotFound());
    }
}