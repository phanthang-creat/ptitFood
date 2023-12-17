package com.server.ptitFood;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={ DataSourceAutoConfiguration.class })
public class PtitFoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtitFoodApplication.class, args);
	}
}