package com.plantastic.backend.service;

import com.plantastic.backend.dto.*;
import com.plantastic.backend.models.entity.Plant;
import com.plantastic.backend.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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

    public void importPlants() throws IOException {
        String listUrl = "https://perenual.com/api/v2/species-list?key=" + apiKey + "&indoor=1";
        PlantListResponse response = restTemplate.getForObject(listUrl, PlantListResponse.class);

        if (response == null || response.getData() == null) {
            System.out.println("❌ Impossible de récupérer la liste des plantes.");
            return;
        }

        List<PlantSummary> listPlantSummary = new ArrayList<>(response.getData());
        System.out.println("-------------------------listPlantSummary-----------------------");
        System.out.println(listPlantSummary.toString());

        PlantSummary summary = listPlantSummary.getFirst(); // ou getFirst() si tu es en Java 21+
        System.out.println("-------------------------summary-----------------------");
        System.out.println(summary.toString());
//        ObjectMapper mapper = new ObjectMapper();
//        PlantSummary summary = mapper.readValue((JsonParser) listPlantSummary, PlantSummary.class);
//        System.out.println(summary.toString());



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

            //Il faut set les données des DTO avant de les injecter dans l'objet en lui même

            Plant plant = new Plant();
            plant.setApiId(summary.getApiId());
            plant.setCommonName(detail.getCommonName());
            plant.setScientificName(detail.getScientificName().isEmpty() ? null : detail.getScientificName().get(0));
            plant.setOtherName(String.join(", ", detail.getOtherName()));
            plant.setFamily(detail.getFamily());
            plant.setWatering(detail.getWatering());
            plant.setLightExposure(detail.getSunlight() != null ? detail.getSunlight() : null);
            plant.setSoil(detail.getSoil());
            plant.setGrowthRate(detail.getGrowthRate());
            plant.setCareLevel(detail.getCareLevel());
            plant.setPoisonousToPet(detail.isPoisonousToPets());
            plant.setDescription(detail.getDescription());
            plant.setImageUrl(detail.getDefaultImage() != null ? detail.getDefaultImage().getOriginalUrl() : null);

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
