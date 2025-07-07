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
import java.util.Optional;

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


        //Boucle for à conserver pour faire les appels sur l'intégralité des données (les 30 plantes qu'on récupère avec un appel api)
        for (PlantSummary summary : response.getData()) {
//        int plantIdToinject = 543;
            try {
                //Pour plusieurs plantes
                PlantDetailResponse detail = restTemplate.getForObject(
                        "https://perenual.com/api/v2/species/details/" + summary.getApiId() + "?key=" + apiKey,
                        PlantDetailResponse.class
                );

                //Pour une seule plante
//                PlantDetailResponse detail = restTemplate.getForObject(
//                        "https://perenual.com/api/v2/species/details/" + plantIdToinject + "?key=" + apiKey,
//                        PlantDetailResponse.class
//                );

                //Pour plusieurs plantes
                CareGuideResponse careGuide = restTemplate.getForObject(
                        "https://perenual.com/api/species-care-guide-list?species_id=" + summary.getApiId() + "&key=" + apiKey,
                        CareGuideResponse.class
                );

                //Pour une seule plante
//                CareGuideResponse careGuide = restTemplate.getForObject(
//                        "https://perenual.com/api/species-care-guide-list?species_id=" + plantIdToinject + "&key=" + apiKey,
//                        CareGuideResponse.class);

                //Il faut set les données des DTO avant de les injecter dans l'objet en lui même

//Pour une seule plante
//               Optional<Plant> existingPlant = plantRepository.findByApiId(plantIdToinject);

                //Pour plusieurs plantes
                Optional<Plant> existingPlant = plantRepository.findByApiId(summary.getApiId());


                Plant plant = existingPlant.orElseGet(Plant::new);
//                plant.setApiId(plantIdToinject);
                plant.setApiId(summary.getApiId());
                plant.setCommonName(detail.getCommonName());
                plant.setScientificName(detail.getScientificName().isEmpty() ? null : detail.getScientificName().get(0));
                plant.setOtherName(String.join(", ", detail.getOtherName()));
                plant.setFamily(detail.getFamily());
                plant.setWatering(detail.getWatering());
                plant.setLightExposure(detail.getSunlight() != null ? String.join(", ", detail.getSunlight()) : null);
                plant.setSoil(String.join(", ",detail.getSoil()));
                plant.setGrowthRate(detail.getGrowthRate());
                plant.setCareLevel(detail.getCareLevel());
                plant.setPoisonousToPet(detail.isPoisonousToPets());
                plant.setDescription(detail.getDescription());
                plant.setImageUrl(detail.getDefaultImage() != null ? detail.getDefaultImage().getOriginalUrl() : null);

                // Care guide
                for (CareGuideItem item : careGuide.getData()) {
                    for (CareGuideDescription careDescription : item.getData()) {
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
                        }
                    }
                }

                plantRepository.save(plant);
                System.out.println("✅ Plant importée : " + plant.getCommonName());

            } catch (Exception e) {
                System.out.println("⚠️ Erreur sur la plante ID " + summary.getApiId() + " : " + e.getMessage());

//                System.out.println("⚠️ Erreur sur la plante ID " + plantIdToinject + " : " + e.getMessage());
            }
            System.out.println("🌿 Import terminé.");
        }
    }
}
