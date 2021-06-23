package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;

import java.util.List;

@Data
public class PersonAndFireStationNumberWhoServedHim {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String age;
    private List<String> medication;
    private List<String> allergies;
    private String fireStationNumber;
}