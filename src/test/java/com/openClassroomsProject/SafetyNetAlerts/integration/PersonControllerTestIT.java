package com.openClassroomsProject.SafetyNetAlerts.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.is;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonControllerTestIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void itTestGetListOfChildrenLivingAtThisAddressAndResponseIsOk() throws Exception {
        mockMvc.perform(get("/childAlert")
                .contentType(MediaType.APPLICATION_JSON)
                .param("address", "1509 Culver St test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("childrenArrayList[0].firstName", is("tTenley")))
                .andExpect(jsonPath("childrenArrayList[0].lastName", is("tBoyd")))
                .andExpect(jsonPath("childrenArrayList[0].age", is("9")));
    }

    @Test
    void itTestGetListOfChildrenLivingAtThisAddressAndResponseIsNotFound() throws Exception {
        mockMvc.perform(get("/childAlert")
                .contentType(MediaType.APPLICATION_JSON)
                .param("address", "Address not found"))
                .andExpect(status().isNotFound());
    }

    @Test
    void itTestGetPersonInformationAndResponseIsOk() throws Exception {
        mockMvc.perform(get("/personInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstName", "tJonanathan")
                .param("lastName", "tMarrack"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is("tJonanathan")))
                .andExpect(jsonPath("lastName", is("tMarrack")))
                .andExpect(jsonPath("address", is("29 15th St test")))
                .andExpect(jsonPath("age", is("32")))
                .andExpect(jsonPath("email", is("drk@email.test")))
                .andExpect(jsonPath("medications", Matchers.empty()))
                .andExpect(jsonPath("allergies", Matchers.empty()));
    }

    @Test
    void itTestGetPersonInformationAndResponseIsNotFound() throws Exception {
        mockMvc.perform(get("/personInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstName", "firstNameTest")
                .param("lastName", "lastNameTest"))
                .andExpect(status().isNotFound());
    }

    @Test
    void itTestGetEmailsOfCityDwellersAndResponseIsOk() throws Exception {
        mockMvc.perform(get("/communityEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .param("city", "tCulver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(23)));
    }

    @Test
    void itTestGetEmailsOfCityDwellersAndResponseIsNotFound() throws Exception {
        mockMvc.perform(get("/communityEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .param("city", "cityTest"))
                .andExpect(status().isNotFound());
    }

    @Test
    void itTestGetPersonsAndResponseIsOk() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(24)));
        ;
    }

    @Test
    void itTestAddNewPersonAndResponseIsCreated() throws Exception {
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
        mockMvc.perform(get("/personInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstName", "firstNameTest")
                .param("lastName", "lastNameTest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is("firstNameTest")))
                .andExpect(jsonPath("lastName", is("lastNameTest")))
                .andExpect(jsonPath("address", is("AddressTest")))
                .andExpect(jsonPath("age", is("?")))
                .andExpect(jsonPath("email", is("emailTest.com")))
                .andExpect(jsonPath("medications", Matchers.empty()))
                .andExpect(jsonPath("allergies", Matchers.empty()));
        mockMvc.perform(delete("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isOk());
    }

    @Test
    void itTestUpdateAnExistingPersonAndResponseIsOk() throws Exception {
        String bodyContent = "{\n" +
                "\"firstName\":\"tJohn\",\n" +
                "\"lastName\":\"tBoyd\",\n" +
                "\"address\":\"addressModify\",\n" +
                "\"city\":\"cityModify\",\n" +
                "\"zip\":\"88888\",\n" +
                "\"phone\":\"444-444-4444\",\n" +
                "\"email\":\"emailModify.modify\"\n" +
                "}";
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isOk());
        mockMvc.perform(get("/personInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstName", "tJohn")
                .param("lastName", "tBoyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is("tJohn")))
                .andExpect(jsonPath("lastName", is("tBoyd")))
                .andExpect(jsonPath("address", is("addressModify")))
                .andExpect(jsonPath("email", is("emailModify.modify")));
    }

    @Test
    void itTestUpdateAnExistingPersonAndResponseIsNotFound() throws Exception {
        String bodyContent = "{\n" +
                "\"firstName\":\"personFirstNameDontExist\",\n" +
                "\"lastName\":\"personLastNameDontExist\",\n" +
                "\"address\":\"addressDontExist\",\n" +
                "\"city\":\"cityDontExist\",\n" +
                "\"zip\":\"00000\",\n" +
                "\"phone\":\"000-000-0000\",\n" +
                "\"email\":\"emailDontExist.com\"\n" +
                "}";
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }

    @Test
    void itTestDeleteAPersonAndResponseIsOk() throws Exception {
        String bodyContentAdd = "{\n" +
                "\"firstName\":\"firstNameTest89\",\n" +
                "\"lastName\":\"lastNameTest89\",\n" +
                "\"address\":\"AddressTest89\",\n" +
                "\"city\":\"cityTest89\",\n" +
                "\"zip\":\"89899\",\n" +
                "\"phone\":\"8989898989\",\n" +
                "\"email\":\"emailTest.89\"\n" +
                "}";
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContentAdd))
                .andExpect(status().isCreated());
        String bodyContentDelete = "{ \"firstName\":\"firstNameTest89\", \"lastName\":\"lastNameTest89\" }";
        mockMvc.perform(delete("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContentDelete))
                .andExpect(status().isOk());
        mockMvc.perform(get("/personInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstName", "firstNameTest89")
                .param("lastName", "lastNameTest89"))
                .andExpect(status().isNotFound());
    }

    @Test
    void itTestDeleteAPersonAndResponseIsNotFound() throws Exception {
        String bodyContent = "{ \"firstName\":\"firstnameDeletedTest\", \"lastName\":\"lastnameDeletedTest\" }";
        mockMvc.perform(delete("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }
}