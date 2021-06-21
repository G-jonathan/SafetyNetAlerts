package com.openClassroomsProject.SafetyNetAlerts.exception;

public class ResourceNotFoundException extends RuntimeException{
    private final String location;
    private final String message;

    public ResourceNotFoundException(String location, String message) {
        this.message = message;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getMessage() {
        return message;
    }
}