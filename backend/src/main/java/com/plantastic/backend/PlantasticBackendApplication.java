package com.plantastic.backend;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.io.IOException;

@Slf4j
@SpringBootApplication
public class PlantasticBackendApplication {

  private final DataSource dataSource;

  // Injection du DataSource par Spring
  public PlantasticBackendApplication(DataSource dataSource) {
    this.dataSource = dataSource;
  }
  
	public static void main(String[] args) throws IOException {
    
		SpringApplication.run(PlantasticBackendApplication.class, args);

		log.info("Server has started with success");
	}

  @PostConstruct
  public void debugDb() {
    try {
      log.info(
        "DB URL = {}",
        dataSource.getConnection().getMetaData().getURL()
      );
    } catch (Exception e) {
      log.error("Unable to retrieve DB URL", e);
    }
  }
}