package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.Person;
import com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel.UniqueIdentifier;
import com.openClassroomsProject.SafetyNetAlerts.service.impl.MedicalRecordServiceImpl;
import com.openClassroomsProject.SafetyNetAlerts.model.dbmodel.MedicalRecord;
import com.openClassroomsProject.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openClassroomsProject.SafetyNetAlerts.repository.PersonRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.ArgumentMatchers.any;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class MedicalRecordServiceTest {

    private static MedicalRecord medicalRecord;
    private static Person person;
    @MockBean
    private MedicalRecordRepository medicalRecordRepository;
    @MockBean
    private IMedicalRecordService medicalRecordService;
    @MockBean
    private PersonRepository personRepository;

    @BeforeEach
    public void setUp() {
        medicalRecordRepository = mock(MedicalRecordRepository.class);
        personRepository = mock(PersonRepository.class);
        medicalRecordService = new MedicalRecordServiceImpl(medicalRecordRepository, personRepository);
        medicalRecord = new MedicalRecord();
        medicalRecord.setId(0);
        medicalRecord.setFirstName("FirstNameTest");
        medicalRecord.setLastName("lastNameTest");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("againstPain : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("WorldHunger : ∞")));
        person = new Person();
        person.setId(0);
        person.setFirstName("test");
        person.setLastName("test");
        person.setAddress("test");
        person.setCity("test");
        person.setZip("00000");
        person.setPhone("000000000");
        person.setEmail("test.com");
    }

    @Test
    public void testGetMedicalRecordsAndReturnIsNotNull(){
        ArrayList<MedicalRecord> medicalRecordArrayList = new ArrayList<>();
        medicalRecordArrayList.add(medicalRecord);
        when(medicalRecordRepository.findAll()).thenReturn(medicalRecordArrayList);
        ArrayList<MedicalRecord> medicalRecordResponse = medicalRecordService.getMedicalRecords();
        assertNotNull(medicalRecordResponse);
        assertEquals(medicalRecordArrayList, medicalRecordResponse);
    }

    @Test
    public void testAddMedicalRecordAndReturnFalseIfPersonDontExist() {
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class),any(String.class))).thenReturn(Optional.empty());
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class),any(String.class))).thenReturn(Optional.empty());
        boolean response = medicalRecordService.addMedicalRecord(medicalRecord);
        assertFalse(response);
    }

    @Test
    public void testAddMedicalRecordReturnFalseIfPersonExistButAlreadyHaveAMedicalRecord() {
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class),any(String.class))).thenReturn(Optional.of(person));
        boolean response = medicalRecordService.addMedicalRecord(medicalRecord);
        assertFalse(response);
    }

    @Test
    public void testAddMedicalRecordReturnTrueIfPersonExistAndDontHaveAMedicalRecordYet() {
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(personRepository.findPersonByFirstNameAndLastName(any(String.class),any(String.class))).thenReturn(Optional.of(person));
        boolean response = medicalRecordService.addMedicalRecord(medicalRecord);
        assertTrue(response);
    }

    @Test
    public void testUpdateAnExistingMedicalRecordWithSuccess() {
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        Optional<MedicalRecord> medicalRecordUpdated = medicalRecordService.updateAnExistingMedicalRecord(medicalRecord);
        assertNotNull(medicalRecordUpdated);
        assertEquals(Optional.of(medicalRecord), medicalRecordUpdated);
    }

    @Test
    public void testUpdateAMedicalRecordWithErrorBecauseHeDoesNotExist() {
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        Optional<MedicalRecord> medicalRecordUpdated = medicalRecordService.updateAnExistingMedicalRecord(medicalRecord);
        assertNotNull(medicalRecordUpdated);
        assertTrue(medicalRecordUpdated.isEmpty());
    }

    @Test
    public void testDeleteAMedicalRecordWithSuccess() {
        UniqueIdentifier uniqueIdentifier = new UniqueIdentifier("firstName", "lastName");
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        boolean isMedicalRecordDeleted = medicalRecordService.deleteAMedicalRecord(uniqueIdentifier);
        assertTrue(isMedicalRecordDeleted);
    }

    @Test
    public void testDeleteAMedicalRecordWithErrorBecauseHeDoeNotExist() {
        UniqueIdentifier uniqueIdentifier = new UniqueIdentifier("firstName", "lastName");
        when(medicalRecordRepository.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        boolean isMedicalRecordDeleted = medicalRecordService.deleteAMedicalRecord(uniqueIdentifier);
        assertFalse(isMedicalRecordDeleted);
    }
}