package com.openClassroomsProject.SafetyNetAlerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openClassroomsProject.SafetyNetAlerts.model.JsonFileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;

@Component
@Profile("test")
public class TestDataSourceConfigImpl implements IDataSourceConfig {

    @Autowired
    ObjectMapper objectMapper;
    @Override
    public JsonFileData setupData() throws IOException {
        return objectMapper.readValue(new File("src/test/resources/dataTest.json"), JsonFileData.class);
    }
}