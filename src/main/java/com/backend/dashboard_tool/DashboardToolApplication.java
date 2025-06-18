package com.backend.dashboard_tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main application class for the Dashboard Tool.
 */
@SpringBootApplication
@EnableCaching
public class DashboardToolApplication {

	/**
	 * Main method to run the Dashboard Tool application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(DashboardToolApplication.class, args);
	}

}
