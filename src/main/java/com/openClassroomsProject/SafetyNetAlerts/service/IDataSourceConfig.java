package com.openClassroomsProject.SafetyNetAlerts.service;

import com.openClassroomsProject.SafetyNetAlerts.model.JsonFileData;
import java.io.IOException;

public interface IDataSourceConfig {

    JsonFileData setupData() throws IOException;
}