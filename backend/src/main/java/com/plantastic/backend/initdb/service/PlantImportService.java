package com.plantastic.backend.initdb.service;

import com.plantastic.backend.initdb.dto.json.PlantDtoFromJson;
import com.plantastic.backend.initdb.dto.api.CareGuideApiResponse;
import com.plantastic.backend.initdb.dto.api.PlantApiSummary;
import com.plantastic.backend.initdb.dto.api.PlantDetailApiResponse;
import com.plantastic.backend.initdb.dto.api.PlantListApiResponse;
import com.plantastic.backend.models.entity.Plant;
import com.plantastic.backend.repository.PlantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PlantImportService {

    private final RestTemplate restTemplate;
    private final PlantRepository plantRepository;

    //Injecté automatiquement par Spring à partir du fichier application.properties.
    @Value("${api.key}")
    private String apiKey;

    public PlantImportService(RestTemplateBuilder builder, PlantRepository plantRepository) {
        this.restTemplate = builder.build();
        this.plantRepository = plantRepository;
    }

    //Une page comporte 30 plantes
    public void importThirtyPlantsFromApi(int page) {
        String listUrl = "https://perenual.com/api/v2/species-list?key=" + apiKey + "&indoor=1&page=" + page;
        PlantListApiResponse response = restTemplate.getForObject(listUrl, PlantListApiResponse.class);

        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            log.warn("❌ Il n'y a plus de plantes à récupérer");
            return;
        }

        List<PlantApiSummary> plantSummaries = new ArrayList<>(response.getData());
        log.debug("🌿 Liste des plantes récupérées : {}", plantSummaries);

        //Boucle for à conserver pour faire les appels sur l'intégralité des données (les 30 plantes qu'on récupère avec un appel api)
        for (PlantApiSummary summary : plantSummaries) {

            importOnePlantFromApi(summary.getApiId());
        }
        log.info("✅ Import terminé pour {} plantes.", plantSummaries.size());
    }

    //Si on souhaite utiliser cette méthode en dehors de cette classe, la mettre en public
    public void importOnePlantFromApi(int apiId) {
        Optional<Plant> plantOpt = createPlantByIdFromApi(apiId);
        if (plantOpt.isPresent()) {
            Plant plant = plantOpt.get();
            plantRepository.save(plant);
            log.info("✅ Plante importée : {}, apiId : {}", plant.getCommonName(), plant.getApiId());
        } else {
            log.error("❌ Échec de l'import : aucune plante trouvée pour apiId {}", apiId);
        }
    }

    private Optional<Plant> createPlantByIdFromApi(int apiId) {
        try {
            //Récupération des détails de la plante
            PlantDetailApiResponse detail = restTemplate.getForObject(
                    "https://perenual.com/api/v2/species/details/" + apiId + "?key=" + apiKey,
                    PlantDetailApiResponse.class
            );

            if (detail == null) {
                log.warn("⚠️Aucun détail trouvé pour l'apiId {}", apiId);
                return Optional.empty();
            }

            // Care guide
            CareGuideApiResponse careGuide = restTemplate.getForObject(
                    "https://perenual.com/api/species-care-guide-list?species_id=" + apiId + "&key=" + apiKey,
                    CareGuideApiResponse.class
            );

            if (careGuide == null || careGuide.getData() == null) {
                log.warn("⚠️Aucun careGuide trouvé pour l'apiId {}", apiId);
            }

            //Vérification de si une plante avec cette apiId existe ou non
            //Si une plante existe, on la récupère, sinon on en créée une nouvelle
            Optional<Plant> existingPlant = plantRepository.findByApiId(apiId);
            Plant plant = existingPlant.orElseGet(() -> new Plant(detail, careGuide, apiId));

            return Optional.of(plant);
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'import de la plante ID {} : {}", apiId, e.getMessage(), e);
            return Optional.empty();
        }
    }

    public void importAllPlantsFromJson(List<PlantDtoFromJson> plantsList) {
        for (PlantDtoFromJson jsonPLant : plantsList) {

            int jsonPlantApiId = (int) jsonPLant.getApiId();

            Optional<Plant> existingPlant = plantRepository.findByApiId(jsonPlantApiId);

            Plant plant = existingPlant.orElseGet(() -> new Plant(jsonPLant));

            if(existingPlant.isPresent()) {
                plant.updatePlantFromDto(jsonPLant);
            }

            plantRepository.save(plant);
            log.info("✅ Plante importée : {}, apiId : {}", plant.getCommonName(), plant.getApiId());
        }
        log.info("✅ Import terminé pour {} plantes.", plantsList.size());
    }
}
