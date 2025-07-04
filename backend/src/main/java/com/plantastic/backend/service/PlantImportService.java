package com.plantastic.backend.service;

import com.plantastic.backend.dto.*;
import com.plantastic.backend.models.entity.Plant;
import com.plantastic.backend.models.types.GrowthRate;
import com.plantastic.backend.models.types.LightExposure;
import com.plantastic.backend.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlantImportService {

    private final RestTemplate restTemplate;
    private final PlantRepository plantRepository;

    @Value("${api.key}")
    private String apiKey;

    public PlantImportService(RestTemplateBuilder builder, PlantRepository repo) {
        this.restTemplate = builder.build();
        this.plantRepository = repo;
    }

    public void importPlants() {
        String listUrl = "https://perenual.com/api/v2/species-list?key=" + apiKey + "&indoor=1";
        PlantListResponse response = restTemplate.getForObject(listUrl, PlantListResponse.class);

        if (response == null || response.getData() == null) {
            System.out.println("❌ Impossible de récupérer la liste des plantes.");
            return;
        }

            List<PlantSummary> listPlantSummary = new ArrayList<>(response.getData());
            PlantSummary summary = listPlantSummary.getFirst();

            //Boucle for à conserver pour faire les appels sur l'intégralité des données (les 30 plantes qu'on récupère avec un appel api)
            //for (PlantSummary summary : response.getData()) {
            try {
                PlantDetailResponse detail = restTemplate.getForObject(
                        "https://perenual.com/api/v2/species/details/" + summary.getApiId() + "?key=" + apiKey,
                        PlantDetailResponse.class
                );

                CareGuideResponse careGuide = restTemplate.getForObject(
                        "https://perenual.com/api/species-care-guide-list?species_id=" + summary.getApiId() + "&key=" + apiKey,
                        CareGuideResponse.class
                );

                Plant plant = new Plant();
                plant.setApiId(summary.getApiId());
                plant.setCommonName(detail.getCommon_name());
                plant.setScientificName(detail.getScientific_name().isEmpty() ? null : detail.getScientific_name().get(0));
                plant.setOtherName(String.join(", ", detail.getOther_name()));
                plant.setFamily(detail.getFamily());
                plant.setWatering(detail.getWatering());
                plant.setLightExposure(detail.getSunlight() != null ? LightExposure.valueOf(String.join(", ", detail.getSunlight())) : null);
                plant.setSoil(detail.getSoil());
                plant.setGrowthRate(GrowthRate.valueOf(detail.getGrowth_rate()));
                plant.setCareLevel(detail.getCare_level());
                plant.setPoisonousToPet(detail.isPoisonous_to_pets());
                plant.setDescription(detail.getDescription());
                plant.setImageUrl(detail.getDefault_image() != null ? detail.getDefault_image().getOriginal_url() : null);

                // Care guide
                for (CareGuideItem item : careGuide.getData()) {
                    switch (item.getType()) {
                        case "water":
                            plant.setWateringDetails(item.getDescription());
                            break;
                        case "sunlight":
                            plant.setSunlightDetails(item.getDescription());
                            break;
                        case "pruning":
                            plant.setPruningDetails(item.getDescription());
                            break;
                    }
                }

                plantRepository.save(plant);
                System.out.println("✅ Plant importée : " + plant.getCommonName());

            } catch (Exception e) {
                System.out.println("⚠️ Erreur sur la plante ID " + summary.getApiId() + " : " + e.getMessage());
            }
        System.out.println("🌿 Import terminé.");
    }
}
