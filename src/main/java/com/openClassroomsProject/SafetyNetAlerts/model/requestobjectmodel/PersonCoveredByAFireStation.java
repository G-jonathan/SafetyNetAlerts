package com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel;

import lombok.Data;

@Data
public class PersonCoveredByAFireStation {
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
}