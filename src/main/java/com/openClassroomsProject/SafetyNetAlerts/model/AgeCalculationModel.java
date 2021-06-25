package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class AgeCalculationModel {
    private String birthdate;
    private String pattern;

    public AgeCalculationModel(String birthdate, String pattern) {
        this.birthdate = birthdate;
        this.pattern = pattern;
    }
}