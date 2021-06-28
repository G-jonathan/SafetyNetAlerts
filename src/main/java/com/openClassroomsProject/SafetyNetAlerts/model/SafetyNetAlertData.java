package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class SafetyNetAlertData {
    private final String FILEPATH = "src/main/resources/json/data.json";

    public String getFILEPATH() {
        return FILEPATH;
    }
}