package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;
import java.util.List;

@Data
public class PersonInformation {
    private String firstName;
    private String lastName;
    private String address;
    private String age;
    private String email;
    private List<String> medications;
    private List<String> allergies;
}
