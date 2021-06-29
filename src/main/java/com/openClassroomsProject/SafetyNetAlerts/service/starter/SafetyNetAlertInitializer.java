package com.openClassroomsProject.SafetyNetAlerts.service.starter;

import com.openClassroomsProject.SafetyNetAlerts.model.JsonFileData;
import org.springframework.stereotype.Component;

@Component
public class SafetyNetAlertInitializer {
    JsonDataService jsonDataService;
    JsonFileData data;

    public SafetyNetAlertInitializer(JsonDataService jsonDataService, JsonFileData data) {
        this.jsonDataService = jsonDataService;
        this.data = data;
    }

    public void start() {
        jsonDataService.savePersons(data.getPersons());
        jsonDataService.saveFireStations(data.getFirestations());
        jsonDataService.saveMedicalRecords(data.getMedicalrecords());
    }
}