package com.openClassroomsProject.SafetyNetAlerts.model.requestobjectmodel;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UniqueIdentifier {
    @NotNull(message = "firstName is mandatory")
    @Size(min = 2, max = 40, message = "firstName must be between 2 and 40 characters long.")
    private final String firstName;
    @NotNull(message = "lastName is mandatory")
    @Size(min = 2, max = 40, message = "lastName must be between 2 and 40 characters long.")
    private final String lastName;

    public UniqueIdentifier(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}