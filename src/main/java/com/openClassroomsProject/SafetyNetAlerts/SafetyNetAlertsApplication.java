package com.openClassroomsProject.SafetyNetAlerts;

import com.openClassroomsProject.SafetyNetAlerts.model.JsonFileData;
import com.openClassroomsProject.SafetyNetAlerts.service.starter.JsonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SafetyNetAlertsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetAlertsApplication.class, args);
	}

	@Autowired
	JsonDataService jsonDataService;
	@Autowired
	IDataSourceConfig dataSourceConfig;
	@Bean
	CommandLineRunner runnerReadJsonWithObjectMapper() {
		return args -> {
			JsonFileData data = dataSourceConfig.setupData();
			SafetyNetAlertInitializer initializer = new SafetyNetAlertInitializer(jsonDataService, data);
			initializer.start();
		};
	}
}