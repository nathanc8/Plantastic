package com.plantastic.backend.service;

import com.plantastic.backend.dto.api.*;
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
            log.error("❌ Impossible de récupérer la liste des plantes depuis l'API.");
            return;
        }

        List<PlantApiSummary> plantSummaries = new ArrayList<>(response.getData());
        log.debug("🌿 Liste des plantes récupérées : {}", plantSummaries);

        //Boucle for à conserver pour faire les appels sur l'intégralité des données (les 30 plantes qu'on récupère avec un appel api)
        for (PlantApiSummary summary : plantSummaries) {

            importOnePlant(summary.getApiId());
        }
        log.info("✅ Import terminé pour {} plantes.", plantSummaries.size());
    }

    //Si on souhaite utiliser cette méthode en dehors de cette classe, la mettre en public
    private void importOnePlant(int apiId) {
        Optional<Plant> plantOpt = createPlantByIdFromApi(apiId);
        if (plantOpt.isPresent()) {
            Plant plant = plantOpt.get();
            plantRepository.save(plant);
            log.info("✅ Plante importée : {}, {}", plant.getCommonName(), plant.getApiId());
        } else {
            log.warn("❌ Échec de l'import : aucune plante trouvée pour apiId {}", apiId);
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
                log.warn("❌ Aucun détail trouvé pour l'apiId {}", apiId);
                return Optional.empty();
            }

            //Vérification de si une plante avec cette apiId existe ou non
            //Si une plante existe, on la récupère, sinon on en créée une nouvelle
            Optional<Plant> existingPlant = plantRepository.findByApiId(apiId);
            Plant plant = existingPlant.orElseGet(Plant::new);

            //Set des attributs de la plante récupérés via detail
            plant.setApiId(apiId);
            plant.setCommonName(detail.getCommonName());
            plant.setScientificName(
                    detail.getScientificName().isEmpty() ? null : detail.getScientificName().getFirst()
            );
            plant.setOtherName(
                    detail.getOtherName() != null ? String.join(", ", detail.getOtherName()) : null
            );
            plant.setFamily(detail.getFamily());
            plant.setWatering(detail.getWatering());
            plant.setLightExposure(
                    detail.getSunlight() != null ? String.join(", ", detail.getSunlight()) : null
            );
            plant.setSoil(
                    detail.getSoil() != null ? String.join(", ", detail.getSoil()) : null
            );
            plant.setGrowthRate(detail.getGrowthRate());
            plant.setCareLevel(detail.getCareLevel());
            plant.setPoisonousToPet(detail.isPoisonousToPets());
            plant.setDescription(detail.getDescription());
            plant.setImageUrl(detail.getDefaultImage() != null ? detail.getDefaultImage().getOriginalUrl() : "");

            // Care guide
            CareGuideApiResponse careGuide = restTemplate.getForObject(
                    "https://perenual.com/api/species-care-guide-list?species_id=" + apiId + "&key=" + apiKey,
                    CareGuideApiResponse.class
            );

            if (careGuide == null || careGuide.getData() == null) {
                log.warn("❌ Aucun careGuide trouvé pour l'apiId {}", apiId);
            } else {
                for (CareGuideApiItem item : careGuide.getData()) {
                    for (CareGuideApiDescription careDescription : item.getData()) {
                        switch (careDescription.getType()) {
                            case "watering":
                                plant.setWateringDetails(careDescription.getDescription());
                                break;
                            case "sunlight":
                                plant.setSunlightDetails(careDescription.getDescription());
                                break;
                            case "pruning":
                                plant.setPruningDetails(careDescription.getDescription());
                                break;
                            default:
                                log.debug("Type de care guide non géré : '{}' pour apiId {}", careDescription.getType(), apiId);
                        }
                    }
                }
            }

            return Optional.of(plant);
        } catch (Exception e) {
            log.error("⚠️ Erreur lors de l'import de la plante ID {} : {}", apiId, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
