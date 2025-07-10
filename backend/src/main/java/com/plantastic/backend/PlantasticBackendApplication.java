package com.plantastic.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class PlantasticBackendApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(PlantasticBackendApplication.class, args);

		System.out.println("hello world");
	}
}