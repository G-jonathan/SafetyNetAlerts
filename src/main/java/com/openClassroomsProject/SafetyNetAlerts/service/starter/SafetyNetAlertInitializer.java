package com.openClassroomsProject.SafetyNetAlerts.service.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openClassroomsProject.SafetyNetAlerts.model.JsonFileData;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;

@Component
public class SafetyNetAlertInitializer {
    JsonDataService jsonDataService;
    String dataFilePath;

    public SafetyNetAlertInitializer() {
    }

    public SafetyNetAlertInitializer(JsonDataService jsonDataService, String dataFilePath) {
        this.jsonDataService = jsonDataService;
        this.dataFilePath = dataFilePath;
    }

    public void start() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFileData jsonFileData = objectMapper.readValue(new File(dataFilePath), JsonFileData.class);
        jsonDataService.savePersons(jsonFileData.getPersons());
        jsonDataService.saveFireStations(jsonFileData.getFirestations());
        jsonDataService.saveMedicalRecords(jsonFileData.getMedicalrecords());

    }
}