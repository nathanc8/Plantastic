package com.plantastic.backend.initdb.runner;

import com.plantastic.backend.initdb.dto.json.PlantDtoFromJson;
import com.plantastic.backend.initdb.service.JsonService;
import com.plantastic.backend.initdb.service.PlantImportService;
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

            //Penser à incrémenter la page de + 1 à chaque appel. On doit se retrouver avec 30 plantes de plus en BDD à chaque appel
            importService.importThirtyPlantsFromApi(4);

//            importService.importOnePlantFromApi(748);

            //Mettre à jour le chemin du fichier JSON à chaque appel.
//            String jsonFilePath = "data/backup/backup_bdd_20250708_plants.json";
//            List<PlantDtoFromJson> plantListFromJson = jsonService.readBackupPlantDbJson(jsonFilePath);
//            importService.importAllPlantsFromJson(plantListFromJson);
        }
    }
}
