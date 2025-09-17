package com.project.sqlConnection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class SqlConnectionApplication {

	@Autowired
	private WeatherExtractor weatherExtractor;
	@Autowired
	private ClearData clearData;

	public static void main(String[] args) {
		SpringApplication.run(SqlConnectionApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void runWeatherExtractorOnStartup() {
		System.out.println("Spring Boot Application started. Initiating Weather Data Extraction...");
		clearData.truncateTable();
		weatherExtractor.executeWeatherExtraction();
		System.out.println("Weather Data Extraction process completed.");
	}
}
