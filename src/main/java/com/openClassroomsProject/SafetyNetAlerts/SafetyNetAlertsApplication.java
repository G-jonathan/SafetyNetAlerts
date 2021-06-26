package com.openClassroomsProject.SafetyNetAlerts;

import com.openClassroomsProject.SafetyNetAlerts.model.SafetyNetAlertData;
import com.openClassroomsProject.SafetyNetAlerts.service.starter.JsonDataService;
import com.openClassroomsProject.SafetyNetAlerts.service.starter.SafetyNetAlertInitializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SafetyNetAlertsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetAlertsApplication.class, args);
	}

	@Bean
	CommandLineRunner runnerReadJsonWithObjectMapper(JsonDataService jsonDataService) {
		return args -> {
			String dataFilePath = new SafetyNetAlertData().getFILEPATH();
			SafetyNetAlertInitializer initializer = new SafetyNetAlertInitializer(jsonDataService, dataFilePath);
			initializer.start();
		};
	}
}