package com.plantastic.backend.runner;

import com.plantastic.backend.models.entity.Plant;
import com.plantastic.backend.service.JsonService;
import com.plantastic.backend.service.PlantImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlantImportRunner implements CommandLineRunner {

  //  private final PlantImportService importService;
    private final JsonService jsonService;

    public PlantImportRunner(JsonService jsonService) {
        //this.importService = importService;
        this.jsonService = jsonService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && args[0].equals("import-plants")) {
            System.out.println("📥 Début de l'import...");
//            importService.importThirtyPlantsFromApi(1);
            String jsonFilePath = "data.backup.backup_bdd_20250708_plants.json";
            List<Plant> plantListFromJson = jsonService.readBackupPlantDbJson(jsonFilePath);
            System.out.println(plantListFromJson);
        }
    }
}
