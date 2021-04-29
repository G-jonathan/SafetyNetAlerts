package com.openClassroomsProject.SafetyNetAlerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openClassroomsProject.SafetyNetAlerts.model.JsonFileData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class SafetyNetAlertsApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SafetyNetAlertsApplication.class, args);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonFileData jsonFileData = objectMapper.readValue(new File("src/main/resources/json/data.json"), JsonFileData.class);
	}
}
