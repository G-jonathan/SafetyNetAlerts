package com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel;

import lombok.Data;
import java.util.List;

@Data
public class HouseHoldMember {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String age;
    private List<String> medication;
    private List<String> allergies;
}