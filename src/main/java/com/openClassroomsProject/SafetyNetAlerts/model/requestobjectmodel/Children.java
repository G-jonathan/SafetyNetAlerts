package com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel;

import lombok.Data;

@Data
public class Children {
    private String firstName;
    private String lastName;
    private String age;

    public Children(String firstName, String lastName, String age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}