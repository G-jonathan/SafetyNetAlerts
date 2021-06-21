package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    @Size(min = 2, max = 40, message = "firstName must be between 2 and 40 characters long.")
    @NotNull(message = "firstName is mandatory")
    private String firstName;
    @Column(name = "last_name")
    @Size(min = 2, max = 40, message = "lastName must be between 2 and 40 characters long.")
    @NotNull(message = "lastName is mandatory")
    private String lastName;
    @NotNull(message = "address is mandatory")
    private String address;
    @NotNull(message = "city is mandatory")
    private String city;
    @Size(min = 5, max = 5)
    @NotNull(message = "zip is mandatory")
    private String zip;
    @NotNull(message = "phone is mandatory")
    private String phone;
    @NotNull(message = "email is mandatory")
    private String email;
}