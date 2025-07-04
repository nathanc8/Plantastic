package com.plantastic.backend.runner;

import com.plantastic.backend.service.PlantImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PlantImportRunner implements CommandLineRunner {

    private final PlantImportService importService;

    public PlantImportRunner(PlantImportService importService) {
        this.importService = importService;
    }

    @Override
    public void run(String... args) throws IOException {
        if (args.length > 0 && args[0].equals("import-plants")) {
            System.out.println("📥 Début de l'import...");
            importService.importPlants();
        }
    }
}
