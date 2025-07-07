package com.plantastic.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlantasticBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantasticBackendApplication.class, args);

		System.out.println("hello world");
	}
}