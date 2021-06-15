package com.openClassroomsProject.SafetyNetAlerts.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionMessage {
    private String path;
    private String method;
    private String timestamp;
    private String status;
    private String error;
    private String message;
    private String details;
    private String help;
}