package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.ChildrenAndOtherMembers;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.PersonInformation;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.PersonRepository;
import static org.mockito.Mockito.*;
import java.util.*;
import com.openClassroomsProject.SafetyNetAlerts.service.impl.PersonServiceImpl;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.ArgumentMatchers.any;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonServiceTest {

    private Person person;
    @MockBean
    private MedicalRecordRepository medicalRecordRepository;
    @MockBean
    private IPersonService personService;
    @MockBean
    private PersonRepository personRepository;

    @BeforeEach
    public void setUp() {
        medicalRecordRepository = mock(MedicalRecordRepository.class);
        personRepository = mock(PersonRepository.class);
        personService = new PersonServiceImpl(personRepository, medicalRecordRepository);
        person = new Person();
        person.setId(1);
        person.setFirstName("personServiceTestFirstName");
        person.setLastName("personServiceTestLastName");
        person.setAddress("personServiceTestAddress");
        person.setCity("personServiceTestCity");
        person.setZip("00000");
        person.setPhone("000000000");
        person.setEmail("personServiceTest.com");
    }

    @Test
    public void testGetPersonAndReturnIsNotNull() {
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        when(personRepository.findAll()).thenReturn(personArrayList);
        ArrayList<Person> personResponse = personService.getPersons();
        assertNotNull(personResponse);
        assertEquals(personArrayList, personResponse);
    }

    @Test
    public void testAddNewPersonWithSuccess() {
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        Optional<Person> personResponse = personService.addNewPerson(person);
        assertNotNull(personResponse);
        assertEquals(Optional.of(person), personResponse);

    }

    @Test
    public void testAddNewPersonWithErrorBecausePersonAlreadyExist() {
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(person));
        Optional<Person> personResponse = personService.addNewPerson(person);
        assertNotNull(personResponse);
        assertTrue(personResponse.isEmpty());
    }

    @Test
    public void updateAnExistingPersonWithSuccess() {
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(person));
        Optional<Person> updateAnExistingPersonWithSuccessResponse = personService.updateAnExistingPerson(person);
        assertNotNull(updateAnExistingPersonWithSuccessResponse);
        assertEquals(Optional.of(person), updateAnExistingPersonWithSuccessResponse);
    }

    @Test
    public void updateAPersonWithErrorBecausePersonDontExist() {
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        Optional<Person> updateAnExistingPersonResponse = personService.updateAnExistingPerson(person);
        assertNotNull(updateAnExistingPersonResponse);
        assertTrue(updateAnExistingPersonResponse.isEmpty());
    }

    @Test
    public void deleteAnExistingPersonWithSuccess() {
        UniqueIdentifier uniqueIdentifier = new UniqueIdentifier("firstName", "lastName");
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(person));
        boolean deleteAnExistingPersonResponse = personService.deleteAPerson(uniqueIdentifier);
        assertTrue(deleteAnExistingPersonResponse);
    }

    @Test
    public void deleteAPersonWithErrorBecausePersonDontExist() {
        UniqueIdentifier uniqueIdentifier = new UniqueIdentifier("firstName", "lastName");
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        boolean deleteAnExistingPersonResponse = personService.deleteAPerson(uniqueIdentifier);
        assertFalse(deleteAnExistingPersonResponse);
    }

    @Test
    public void getEmailsOfCityDwellersAndReturnAListWithOneOrMoreMails() {
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        ArrayList<String> emailList = new ArrayList<>(Collections.singleton(person.getEmail()));
        when(personRepository.findPersonByCity(any(String.class))).thenReturn(personArrayList);
        ArrayList<String> getEmailsOfCityDwellersResponse = personService.getEmailsOfCityDwellers(person.getCity());
        assertEquals(emailList, getEmailsOfCityDwellersResponse);
    }

    @Test
    public void getEmailsOfCityDwellersAndReturnAnEmptyListBecauseCityNotFound() {
        String city = "stringTest";
        when(personRepository.findPersonByCity(any(String.class))).thenReturn(new ArrayList<>());
        ArrayList<String> getEmailsOfCityDwellersResponse = personService.getEmailsOfCityDwellers(city);
        assertNotNull(getEmailsOfCityDwellersResponse);
        assertTrue(getEmailsOfCityDwellersResponse.isEmpty());
    }

    @Test
    public void getPersonInformationWithSuccessWithPersonExistAndHaveAMedicalRecord() {
        String firstName = "personServiceTestFirstName";
        String lastName = "personServiceTestLastName";
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(0);
        medicalRecord.setFirstName("FirstNameTest");
        medicalRecord.setLastName("lastNameTest");
        medicalRecord.setBirthdate("10/18/1985");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("againstPain : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("WorldHunger : ∞")));
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(person));
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        Optional<PersonInformation> getPersonInformationResponse = personService.getPersonInformation(firstName, lastName);
        assertTrue(getPersonInformationResponse.isPresent());
        assertEquals("personServiceTestFirstName", getPersonInformationResponse.get().getFirstName());
        assertEquals("personServiceTestLastName", getPersonInformationResponse.get().getLastName());
        assertEquals("personServiceTestAddress", getPersonInformationResponse.get().getAddress());
        assertEquals("personServiceTest.com", getPersonInformationResponse.get().getEmail());
        assertEquals(medicalRecord.getMedications(), getPersonInformationResponse.get().getMedications());
        assertEquals(medicalRecord.getAllergies(), getPersonInformationResponse.get().getAllergies());
        assertEquals("35", getPersonInformationResponse.get().getAge());
    }

    @Test
    public void getPersonInformationWithSuccessWithPersonExistAndDontHaveAMedicalRecord() {
        String firstName = "personServiceTestFirstName";
        String lastName = "personServiceTestLastName";
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(0);
        medicalRecord.setFirstName("FirstNameTest");
        medicalRecord.setLastName("lastNameTest");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("againstPain : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("WorldHunger : ∞")));
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(person));
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        Optional<PersonInformation> getPersonInformationResponse = personService.getPersonInformation(firstName, lastName);
        assertTrue(getPersonInformationResponse.isPresent());
        assertEquals("personServiceTestFirstName", getPersonInformationResponse.get().getFirstName());
        assertEquals("personServiceTestLastName", getPersonInformationResponse.get().getLastName());
        assertEquals("personServiceTestAddress", getPersonInformationResponse.get().getAddress());
        assertEquals("personServiceTest.com", getPersonInformationResponse.get().getEmail());
        assertEquals(new ArrayList<>(), getPersonInformationResponse.get().getMedications());
        assertEquals(new ArrayList<>(), getPersonInformationResponse.get().getAllergies());
        assertEquals("?", getPersonInformationResponse.get().getAge());
    }

    @Test
    public void getPersonInformationAndReturnAndEmptyObjectBecausePersonDontExist() {
        String firstName = "personServiceTestFirstName";
        String lastName = "personServiceTestLastName";
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        Optional<PersonInformation> getPersonInformationResponse = personService.getPersonInformation(firstName, lastName);
        assertNotNull(getPersonInformationResponse);
        assertTrue(getPersonInformationResponse.isEmpty());
    }

    @Test
    public void getListOfChildrenLivingAtThisAddressAndReturnWithSuccess() {
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(0);
        medicalRecord.setFirstName("FirstNameTest");
        medicalRecord.setLastName("lastNameTest");
        medicalRecord.setBirthdate("03/06/2020");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("againstPain : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("WorldHunger : ∞")));
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        IPersonService personServicePartialMock = mock(PersonServiceImpl.class);
        when(personServicePartialMock.buildChildrenAndOtherMembers(personArrayList)).thenCallRealMethod();
        when(personRepository.findPersonByAddress(any(String.class))).thenReturn(personArrayList);
        Optional<ChildrenAndOtherMembers> childrenAndOtherMembersResponse = personService.getListOfChildrenLivingAtThisAddress("addressTestListOfChildrenMethod");
        assertNotNull(childrenAndOtherMembersResponse);
        assertTrue(childrenAndOtherMembersResponse.isPresent());
        assertFalse(childrenAndOtherMembersResponse.get().getChildrenArrayList().isEmpty());
        assertTrue(childrenAndOtherMembersResponse.get().getOtherHouseHoldMembers().isEmpty());
        assertEquals("1", childrenAndOtherMembersResponse.get().getChildrenArrayList().get(0).getAge());
        assertEquals("personServiceTestFirstName", childrenAndOtherMembersResponse.get().getChildrenArrayList().get(0).getFirstName());
        assertEquals("personServiceTestLastName", childrenAndOtherMembersResponse.get().getChildrenArrayList().get(0).getLastName());
    }

    @Test
    public void getListOfChildrenLivingAtThisAddressAndReturnAnEmptyObjectBecauseThereIsNoChildren() {
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(0);
        medicalRecord.setFirstName("FirstNameTest");
        medicalRecord.setLastName("lastNameTest");
        medicalRecord.setBirthdate("03/06/1900");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("againstPain : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("WorldHunger : ∞")));
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        IPersonService personServicePartialMock = mock(PersonServiceImpl.class);
        when(personServicePartialMock.buildChildrenAndOtherMembers(personArrayList)).thenCallRealMethod();
        when(personRepository.findPersonByAddress(any(String.class))).thenReturn(personArrayList);
        Optional<ChildrenAndOtherMembers> childrenAndOtherMembersResponse = personService.getListOfChildrenLivingAtThisAddress("addressTestListOfChildrenMethod");
        assertNotNull(childrenAndOtherMembersResponse);
        assertTrue(childrenAndOtherMembersResponse.isPresent());
        assertTrue(childrenAndOtherMembersResponse.get().getChildrenArrayList().isEmpty());
        assertTrue(childrenAndOtherMembersResponse.get().getOtherHouseHoldMembers().isEmpty());
    }

    @Test
    public void getListOfChildrenLivingAtThisAddressAndReturnAnEmptyObjectBecauseAddressNotFound() {
        String addressTestListOfChildrenMethod = "addressTestListOfChildrenMethod";
        when(personRepository.findPersonByAddress(any(String.class))).thenReturn(new ArrayList<>());
        Optional<ChildrenAndOtherMembers> childrenAndOtherMembersResponse = personService.getListOfChildrenLivingAtThisAddress(addressTestListOfChildrenMethod);
        assertNotNull(childrenAndOtherMembersResponse);
        assertTrue(childrenAndOtherMembersResponse.isEmpty());
    }

    @Test
    public void buildChildrenAndOtherMembersWithSuccessWithoutChildren() {
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(0);
        medicalRecord.setFirstName("FirstNameTest");
        medicalRecord.setLastName("lastNameTest");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("againstPain : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("WorldHunger : ∞")));
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        ChildrenAndOtherMembers buildChildrenAndOtherMembersResponse = personService.buildChildrenAndOtherMembers(personArrayList);
        assertNotNull(buildChildrenAndOtherMembersResponse);
        assertEquals("personServiceTestFirstName", buildChildrenAndOtherMembersResponse.getOtherHouseHoldMembers().get(0).getFirstName());
        assertEquals("personServiceTestLastName", buildChildrenAndOtherMembersResponse.getOtherHouseHoldMembers().get(0).getLastName());
        assertTrue(buildChildrenAndOtherMembersResponse.getChildrenArrayList().isEmpty());
    }

    @Test
    public void buildChildrenAndOtherMembersWithSuccessWithChildren() {
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(0);
        medicalRecord.setFirstName("FirstNameTest");
        medicalRecord.setLastName("lastNameTest");
        medicalRecord.setBirthdate("03/06/2020");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("againstPain : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("WorldHunger : ∞")));
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        ChildrenAndOtherMembers buildChildrenAndOtherMembersResponse = personService.buildChildrenAndOtherMembers(personArrayList);
        assertNotNull(buildChildrenAndOtherMembersResponse);
        assertEquals("1", buildChildrenAndOtherMembersResponse.getChildrenArrayList().get(0).getAge());
        assertEquals("personServiceTestFirstName", buildChildrenAndOtherMembersResponse.getChildrenArrayList().get(0).getFirstName());
        assertEquals("personServiceTestLastName", buildChildrenAndOtherMembersResponse.getChildrenArrayList().get(0).getLastName());
    }
}