package com.openClassroomsProject.SafetyNetAlerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openClassroomsProject.SafetyNetAlerts.model.JsonFileData;
import com.openClassroomsProject.SafetyNetAlerts.service.JsonDataService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.File;

@SpringBootApplication
public class SafetyNetAlertsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetAlertsApplication.class, args);
	}

	@Bean
	CommandLineRunner runnerReadJsonWithObjectMapper(JsonDataService jsonDataService) {
		return args -> {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonFileData jsonFileData = objectMapper.readValue(new File("src/main/resources/json/data.json"), JsonFileData.class);
			jsonDataService.savePersons(jsonFileData.getPersons());
			jsonDataService.saveFireStations(jsonFileData.getFirestations());
			jsonDataService.saveMedicalRecords(jsonFileData.getMedicalrecords());
		};
		/* /TODO
			SafetyNetAlertData data = SafetyNetAlertDataProvider.provideData();
			SafetyNetAlertInitializer initializer = new SafetynetAlertInitializer(data);
			initializer.start();
		*/
	}
}