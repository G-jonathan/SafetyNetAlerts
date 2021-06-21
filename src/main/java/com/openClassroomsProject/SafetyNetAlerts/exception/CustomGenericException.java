package com.openClassroomsProject.SafetyNetAlerts.exception;

public class CustomGenericException extends RuntimeException {
    private final Exception exception;
    private final String location;

    public CustomGenericException(String location, Exception exception) {
        this.location = location;
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public String getLocation() {
        return location;
    }
}