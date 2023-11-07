package com.server.ptitFood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PtitFoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtitFoodApplication.class, args);
	}
}