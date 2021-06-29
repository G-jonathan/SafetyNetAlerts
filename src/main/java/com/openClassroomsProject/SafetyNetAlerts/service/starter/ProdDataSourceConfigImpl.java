package com.openClassroomsProject.SafetyNetAlerts.service.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openClassroomsProject.SafetyNetAlerts.model.JsonFileData;
import com.openClassroomsProject.SafetyNetAlerts.service.IDataSourceConfig;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;

@Service
@Profile("dev")
public class ProdDataSourceConfigImpl implements IDataSourceConfig {

    @Override
    public JsonFileData setupData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File("src/main/resources/json/data.json"), JsonFileData.class);
    }
}