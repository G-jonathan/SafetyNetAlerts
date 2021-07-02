package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.FireStation;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.*;
import com.openClassroomsProject.SafetyNetAlerts.repository.FireStationRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.PersonRepository;
import com.openClassroomsProject.SafetyNetAlerts.service.impl.FireStationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class FireStationServiceTest {

    private FireStation fireStation;
    @MockBean
    private PersonRepository personRepository;
    @MockBean
    private MedicalRecordRepository medicalRecordRepository;
    @MockBean
    private FireStationRepository fireStationRepository;
    @MockBean
    private IFireStationService fireStationService;

    @BeforeEach
    public void setUp() {
        medicalRecordRepository = mock(MedicalRecordRepository.class);
        personRepository = mock(PersonRepository.class);
        fireStationRepository = mock(FireStationRepository.class);
        fireStationService = new FireStationServiceImpl(fireStationRepository, personRepository, medicalRecordRepository);
        fireStation = new FireStation();
        fireStation.setId(0);
        fireStation.setStation("99");
        fireStation.setAddress("fireStationServiceTestAddress");
    }

    @Test
    public void testGetFireStationsAndReturnIsNotNull() {
        ArrayList<FireStation> fireStationArrayList = new ArrayList<>();
        fireStationArrayList.add(fireStation);
        when(fireStationRepository.findAll()).thenReturn(fireStationArrayList);
        ArrayList<FireStation> getFireStationsResponse = fireStationService.getFireStations();
        assertNotNull(getFireStationsResponse);
        assertEquals(fireStationArrayList, getFireStationsResponse);
    }

    @Test
    public void addStationAndAddressMappingWithErrorBecauseAddressAlreadyExist() {
        when(fireStationRepository.findByAddress(any(String.class))).thenReturn(Optional.of(fireStation));
        Optional<FireStation> addStationAndAddressMappingResponse = fireStationService.addStationAndAddressMapping(fireStation);
        assertNotNull(addStationAndAddressMappingResponse);
        assertTrue(addStationAndAddressMappingResponse.isEmpty());
    }

    @Test
    public void addStationAndAddressMappingWithSuccess() {
        when(fireStationRepository.findByAddress(any(String.class))).thenReturn(Optional.empty());
        Optional<FireStation> addStationAndAddressMappingResponse = fireStationService.addStationAndAddressMapping(fireStation);
        assertNotNull(addStationAndAddressMappingResponse);
        assertEquals(Optional.of(fireStation), addStationAndAddressMappingResponse);
    }

    @Test
    public void updateFireStationNumberOfAnAddressWithSuccess() {
        when(fireStationRepository.findByAddress(any(String.class))).thenReturn(Optional.of(fireStation));
        Optional<FireStation> updateFireStationNumberOfAnAddressResponse = fireStationService.updateFireStationNumberOfAnAddress(fireStation);
        assertNotNull(updateFireStationNumberOfAnAddressResponse);
        assertEquals(Optional.of(fireStation), updateFireStationNumberOfAnAddressResponse);
    }

    @Test
    public void updateFireStationNumberOfAnAddressWithErrorBecauseFireStationNotFound() {
        when(fireStationRepository.findByAddress(any(String.class))).thenReturn(Optional.empty());
        Optional<FireStation> updateFireStationNumberOfAnAddressResponse = fireStationService.updateFireStationNumberOfAnAddress(fireStation);
        assertNotNull(updateFireStationNumberOfAnAddressResponse);
        assertTrue(updateFireStationNumberOfAnAddressResponse.isEmpty());
    }

    @Test
    public void deleteMappingOfAStationWithErrorBecauseFireStationNumberDontExist() {
        when(fireStationRepository.findByStation(any(String.class))).thenReturn(Optional.empty());
        Optional<List<FireStation>> deleteMappingOfAStationResponse = fireStationService.deleteMappingOfAStation("99");
        assertNotNull(deleteMappingOfAStationResponse);
        assertTrue(deleteMappingOfAStationResponse.isEmpty());
    }

    @Test
    public void deleteMappingOfAStationWithSuccess() {
        Optional<List<FireStation>> fireStationList = Optional.of(new ArrayList<>());
        fireStationList.get().add(fireStation);
        when(fireStationRepository.findByStation(any(String.class))).thenReturn(fireStationList);
        Optional<List<FireStation>> deleteMappingOfAStationResponse = fireStationService.deleteMappingOfAStation("99");
        assertNotNull(deleteMappingOfAStationResponse);
        assertEquals(fireStationList, deleteMappingOfAStationResponse);
    }

    @Test
    public void getPhoneNumbersPersonServedByAFireStationWithErrorBecauseNoFireStationIsMappedWithThisNumber() {
        when(fireStationRepository.findFireStationByStation(any(String.class))).thenReturn(new ArrayList<>());
        ArrayList<String> getPhoneNumbersPersonServedByAFireStationResponse = fireStationService.getPhoneNumbersPersonServedByAFireStation("99");
        assertNotNull(getPhoneNumbersPersonServedByAFireStationResponse);
        assertTrue(getPhoneNumbersPersonServedByAFireStationResponse.isEmpty());
    }

    @Test
    public void getPhoneNumbersPersonServedByAFireStationWithSuccess() {
        Person person = new Person();
        person.setId(1);
        person.setFirstName("fireStationServiceTestFirstName");
        person.setLastName("fireStationServiceTestLastName");
        person.setAddress("fireStationServiceTestAddress");
        person.setCity("fireStationServiceTestCity");
        person.setZip("99999");
        person.setPhone("123-456-7890");
        person.setEmail("FireStationServiceTest.com");
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        ArrayList<FireStation> fireStationArrayList = new ArrayList<>();
        fireStationArrayList.add(fireStation);
        when(fireStationRepository.findFireStationByStation(any(String.class))).thenReturn(fireStationArrayList);
        when(personRepository.findPersonByAddress(any(String.class))).thenReturn(personArrayList);
        ArrayList<String> getPhoneNumbersPersonServedByAFireStationResponse = fireStationService.getPhoneNumbersPersonServedByAFireStation("99");
        assertNotNull(getPhoneNumbersPersonServedByAFireStationResponse);
        assertEquals("123-456-7890", getPhoneNumbersPersonServedByAFireStationResponse.get(0));
    }

    @Test
    public void getPersonListCoveredByAFireStationWithErrorBecauseNoFireStationContainsThisNumber() {
        when(fireStationRepository.findFireStationByStation(any(String.class))).thenReturn(new ArrayList<>());
        Optional<PersonListCoveredByAFireStation> getPersonListCoveredByAFireStationResponse = fireStationService.getPersonListCoveredByAFireStation("99");
        assertNotNull(getPersonListCoveredByAFireStationResponse);
        assertTrue(getPersonListCoveredByAFireStationResponse.isEmpty());
    }

    @Test
    public void getPersonListCoveredByAFireStationWithSuccess() {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(0);
        medicalRecord.setFirstName("FirstNameTest");
        medicalRecord.setLastName("lastNameTest");
        medicalRecord.setBirthdate("03/06/1900");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("againstPain : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("WorldHunger : ∞")));
        Person person = new Person();
        person.setId(1);
        person.setFirstName("fireStationServiceTestFirstName");
        person.setLastName("fireStationServiceTestLastName");
        person.setAddress("fireStationServiceTestAddress");
        person.setCity("fireStationServiceTestCity");
        person.setZip("99999");
        person.setPhone("123-456-7890");
        person.setEmail("FireStationServiceTest.com");
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        ArrayList<FireStation> fireStationArrayList = new ArrayList<>();
        fireStationArrayList.add(fireStation);
        UniqueIdentifier uniqueIdentifier = new UniqueIdentifier(person.getFirstName(), person.getLastName());
        ArrayList<UniqueIdentifier> uniqueIdentifierArrayList = new ArrayList<>();
        uniqueIdentifierArrayList.add(uniqueIdentifier);
        IFireStationService fireStationServicePartialMock = mock(FireStationServiceImpl.class);
        when(fireStationRepository.findFireStationByStation(any(String.class))).thenReturn(fireStationArrayList);
        when(personRepository.findPersonByAddress(any(String.class))).thenReturn(personArrayList);
        when(fireStationServicePartialMock.calculationOfTheNumberOfAdultsAndChildren(uniqueIdentifierArrayList)).thenCallRealMethod();
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        Optional<PersonListCoveredByAFireStation> getPersonListCoveredByAFireStationResponse = fireStationService.getPersonListCoveredByAFireStation("99");
        assertNotNull(getPersonListCoveredByAFireStationResponse);
        assertTrue(getPersonListCoveredByAFireStationResponse.isPresent());
        assertEquals("1", getPersonListCoveredByAFireStationResponse.get().getNumberOfAdultsAndChildren().get("Adults"));
        assertEquals("0", getPersonListCoveredByAFireStationResponse.get().getNumberOfAdultsAndChildren().get("Children"));
    }

    @Test
    public void calculationOfTheNumberOfAdultsAndChildrenWithSuccess() {
        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setBirthdate("03/06/1900");
        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setBirthdate("03/06/2018");
        UniqueIdentifier uniqueIdentifier1 = new UniqueIdentifier("firstNameUniqueIdentifier1", "lastNameUniqueIdentifier1");
        UniqueIdentifier uniqueIdentifier2 = new UniqueIdentifier("firstNameUniqueIdentifier2", "lastNameUniqueIdentifier2");
        ArrayList<UniqueIdentifier> uniqueIdentifierArrayList = new ArrayList<>();
        uniqueIdentifierArrayList.add(uniqueIdentifier1);
        uniqueIdentifierArrayList.add(uniqueIdentifier2);
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName("firstNameUniqueIdentifier1", "lastNameUniqueIdentifier1")).thenReturn(Optional.of(medicalRecord1));
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName("firstNameUniqueIdentifier2", "lastNameUniqueIdentifier2")).thenReturn(Optional.of(medicalRecord2));
        HashMap<String, String> calculationOfTheNumberOfAdultsAndChildrenResponse = fireStationService.calculationOfTheNumberOfAdultsAndChildren(uniqueIdentifierArrayList);
        assertNotNull(calculationOfTheNumberOfAdultsAndChildrenResponse);
        assertEquals("1", calculationOfTheNumberOfAdultsAndChildrenResponse.get("Adults"));
        assertEquals("1", calculationOfTheNumberOfAdultsAndChildrenResponse.get("Children"));
    }

    @Test
    public void getPersonListAndHerFireStationNumberReturnEmptyIfNothingFoundForThisAddress() {
        ArrayList<Person> personArrayList = new ArrayList<>();
        when(personRepository.findPersonByAddress(any(String.class))).thenReturn(personArrayList);
        ArrayList<PersonAndFireStationNumberWhoServedHim> getPersonListAndHerFireStationNumberResponse = fireStationService.getPersonListAndHerFireStationNumber("fireStationServiceTestAddress");
        assertNotNull(getPersonListAndHerFireStationNumberResponse);
        assertTrue(getPersonListAndHerFireStationNumberResponse.isEmpty());
    }

    @Test
    public void getPersonListAndHerFireStationNumberWithSuccess() {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(0);
        medicalRecord.setFirstName("fireStationServiceTestFirstName");
        medicalRecord.setLastName("fireStationServiceTestLastName");
        medicalRecord.setBirthdate("03/06/1900");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("againstPain : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("WorldHunger : ∞")));
        Person person = new Person();
        person.setId(1);
        person.setFirstName("fireStationServiceTestFirstName");
        person.setLastName("fireStationServiceTestLastName");
        person.setAddress("fireStationServiceTestAddress");
        person.setCity("fireStationServiceTestCity");
        person.setZip("99999");
        person.setPhone("888-888-8888");
        person.setEmail("FireStationServiceTest.com");
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        when(personRepository.findPersonByAddress(any(String.class))).thenReturn(personArrayList);
        when(fireStationRepository.findByAddress(any(String.class))).thenReturn(Optional.of(fireStation));
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        ArrayList<PersonAndFireStationNumberWhoServedHim> getPersonListAndHerFireStationNumberResponse = fireStationService.getPersonListAndHerFireStationNumber("fireStationServiceTestAddress");
        assertNotNull(getPersonListAndHerFireStationNumberResponse);
        assertEquals("fireStationServiceTestFirstName", getPersonListAndHerFireStationNumberResponse.get(0).getFirstName());
        assertEquals("fireStationServiceTestLastName", getPersonListAndHerFireStationNumberResponse.get(0).getLastName());
        assertEquals("888-888-8888", getPersonListAndHerFireStationNumberResponse.get(0).getPhoneNumber());
        assertEquals("121", getPersonListAndHerFireStationNumberResponse.get(0).getAge());
        assertEquals("99", getPersonListAndHerFireStationNumberResponse.get(0).getFireStationNumber());
    }

    @Test
    public void getListOfHomesServedByThisStationsWithSuccess() {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(0);
        medicalRecord.setFirstName("fireStationServiceTestFirstName");
        medicalRecord.setLastName("fireStationServiceTestLastName");
        medicalRecord.setBirthdate("03/06/1900");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("againstPain : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("WorldHunger : ∞")));
        Person person = new Person();
        person.setId(1);
        person.setFirstName("fireStationServiceTestFirstName");
        person.setLastName("fireStationServiceTestLastName");
        person.setAddress("fireStationServiceTestAddress");
        person.setCity("fireStationServiceTestCity");
        person.setZip("99999");
        person.setPhone("888-888-8888");
        person.setEmail("FireStationServiceTest.com");
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        ArrayList<FireStation> fireStationArrayList = new ArrayList<>();
        fireStationArrayList.add(fireStation);
        ArrayList<String> stationNumbers = new ArrayList<>(Arrays.asList("50", "60"));
        when(fireStationRepository.findFireStationByStation(any(String.class))).thenReturn(fireStationArrayList);
        when(personRepository.findPersonByAddress(any(String.class))).thenReturn(personArrayList);
        IFireStationService fireStationServicePartialMock = mock(FireStationServiceImpl.class);
        when(fireStationServicePartialMock.buildHouseHoldMemberArrayList(personArrayList)).thenCallRealMethod();
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        ArrayList<HouseHold> getListOfHomesServedByThisStationsResponse = fireStationService.getListOfHomesServedByThisStations(stationNumbers);
        assertNotNull(getListOfHomesServedByThisStationsResponse);
        assertEquals("fireStationServiceTestAddress", getListOfHomesServedByThisStationsResponse.get(0).getAddress());
        assertEquals("fireStationServiceTestFirstName", getListOfHomesServedByThisStationsResponse.get(0).getHouseHoldMemberArrayList().get(0).getFirstName());
        assertEquals("fireStationServiceTestLastName", getListOfHomesServedByThisStationsResponse.get(0).getHouseHoldMemberArrayList().get(0).getLastName());
        assertEquals("888-888-8888", getListOfHomesServedByThisStationsResponse.get(0).getHouseHoldMemberArrayList().get(0).getPhoneNumber());
        assertEquals("121", getListOfHomesServedByThisStationsResponse.get(0).getHouseHoldMemberArrayList().get(0).getAge());
        assertEquals("WorldHunger : ∞", getListOfHomesServedByThisStationsResponse.get(0).getHouseHoldMemberArrayList().get(0).getAllergies().get(0));
        assertEquals("againstPain : 12g", getListOfHomesServedByThisStationsResponse.get(0).getHouseHoldMemberArrayList().get(0).getMedication().get(0));
    }

    @Test
    public void getListOfHomesServedByThisStationsReturnAnEmptyListBecauseNothingFoundForThisStation() {
        ArrayList<String> stationNumbers = new ArrayList<>(Arrays.asList("50", "60"));
        when(fireStationRepository.findFireStationByStation(any(String.class))).thenReturn(new ArrayList<>());
        ArrayList<HouseHold> getListOfHomesServedByThisStationsResponse = fireStationService.getListOfHomesServedByThisStations(stationNumbers);
        assertNotNull(getListOfHomesServedByThisStationsResponse);
        assertTrue(getListOfHomesServedByThisStationsResponse.isEmpty());
    }

    @Test
    public void buildHouseHoldMemberArrayListWithSuccess() {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(0);
        medicalRecord.setFirstName("fireStationServiceTestFirstName");
        medicalRecord.setLastName("fireStationServiceTestLastName");
        medicalRecord.setBirthdate("03/06/1900");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("againstPain : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("WorldHunger : ∞")));
        Person person = new Person();
        person.setId(1);
        person.setFirstName("fireStationServiceTestFirstName");
        person.setLastName("fireStationServiceTestLastName");
        person.setAddress("fireStationServiceTestAddress");
        person.setCity("fireStationServiceTestCity");
        person.setZip("99999");
        person.setPhone("888-888-8888");
        person.setEmail("FireStationServiceTest.com");
        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        ArrayList<HouseHoldMember> buildHouseHoldMemberArrayListResponse = fireStationService.buildHouseHoldMemberArrayList(personArrayList);
        System.out.println("5555555555555555555" + buildHouseHoldMemberArrayListResponse);
        assertNotNull(buildHouseHoldMemberArrayListResponse);
        assertEquals("fireStationServiceTestFirstName", buildHouseHoldMemberArrayListResponse.get(0).getFirstName());
        assertEquals("fireStationServiceTestLastName", buildHouseHoldMemberArrayListResponse.get(0).getLastName());
        assertEquals("121", buildHouseHoldMemberArrayListResponse.get(0).getAge());
        assertEquals("888-888-8888", buildHouseHoldMemberArrayListResponse.get(0).getPhoneNumber());
        assertEquals("WorldHunger : ∞", buildHouseHoldMemberArrayListResponse.get(0).getAllergies().get(0));
        assertEquals("againstPain : 12g", buildHouseHoldMemberArrayListResponse.get(0).getMedication().get(0));
    }
}