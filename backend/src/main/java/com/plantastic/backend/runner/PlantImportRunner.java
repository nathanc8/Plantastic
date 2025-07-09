package com.plantastic.backend.runner;

import com.plantastic.backend.dto.json.PlantDtoFromJson;
import com.plantastic.backend.service.JsonService;
import com.plantastic.backend.service.PlantImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlantImportRunner implements CommandLineRunner {

    private final PlantImportService importService;
    private final JsonService jsonService;

    public PlantImportRunner(PlantImportService importService, JsonService jsonService) {
        this.importService = importService;
        this.jsonService = jsonService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && args[0].equals("import-plants")) {
            System.out.println("📥 Début de l'import...");
//            importService.importThirtyPlantsFromApi(1);

            String jsonFilePath = "data/backup/backup_bdd_20250708_plants.json";
            List<PlantDtoFromJson> plantListFromJson = jsonService.readBackupPlantDbJson(jsonFilePath);
            importService.importAllPlantsFromJson(plantListFromJson);
        }
    }
}
