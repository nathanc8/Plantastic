package com.plantastic.backend.models.entity;

import com.plantastic.backend.dto.api.CareGuideApiDescription;
import com.plantastic.backend.dto.api.CareGuideApiItem;
import com.plantastic.backend.dto.api.CareGuideApiResponse;
import com.plantastic.backend.dto.api.PlantDetailApiResponse;
import com.plantastic.backend.dto.json.PlantDtoFromJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "plants")
@Slf4j
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "api_id")
    private long apiId;
    @Column(name = "common_name", nullable = false)
    private String commonName;
    @Column(name = "other_name")
    private String otherName;
    @Column(name = "scientific_name", nullable = false)
    private String scientificName;
    @Column(nullable = false)
    private String family;
    @Column(nullable = false, length = 2000)
    private String description;
    @Column(name = "care_level")
    private String careLevel;
    @Column
    private String imageUrl;
    @Column(nullable = false)
    private String watering;
    @Column
    private String soil;
    @Column(name = "light_exposure")
    private String lightExposure;
    @Column(name = "growth_rate")
    private String growthRate;
    @Column(name = "poisonous_to_pet")
    private boolean poisonousToPet;
    @Column(name = "watering_details", length = 2000)
    private String wateringDetails;
    @Column(name = "sunlight_details", length = 2000)
    private String sunlightDetails;
    @Column(name = "pruning_details", length = 2000)
    private String pruningDetails;

    //Constructeur qui nous permet de créer une entité à partir du DTO Json
    public Plant(PlantDtoFromJson dto) {
        this.apiId = dto.getApiId();
        this.commonName = emptyStringToNull(dto.getCommonName());
        this.otherName = emptyStringToNull(dto.getOtherName());
        this.scientificName = emptyStringToNull(dto.getScientificName());
        this.family = emptyStringToNull(dto.getFamily());
        this.description = emptyStringToNull(dto.getDescription());
        this.careLevel = emptyStringToNull(dto.getCareLevel());
        this.imageUrl = emptyStringToNull(dto.getImageUrl());
        this.watering = emptyStringToNull(dto.getWatering());
        this.soil = emptyStringToNull(dto.getSoil());
        this.lightExposure = emptyStringToNull(dto.getLightExposure());
        this.growthRate = emptyStringToNull(dto.getGrowthRate());
        this.poisonousToPet = dto.isPoisonousToPet();
        this.wateringDetails = emptyStringToNull(dto.getWateringDetails());
        this.sunlightDetails = emptyStringToNull(dto.getSunlightDetails());
        this.pruningDetails = emptyStringToNull(dto.getPruningDetails());
    }

    //Constructeur qui nous permet de créer une entité à partir des données de l'API
    public Plant(PlantDetailApiResponse detailPlant, CareGuideApiResponse careGuide, int apiId) {
        //Set des attributs de la plante récupérés via detail
        this.setApiId(apiId);
        this.setCommonName(detailPlant.getCommonName());
        this.setScientificName(
                detailPlant.getScientificName().isEmpty() ? null : detailPlant.getScientificName().getFirst()
        );
        this.setOtherName(
                detailPlant.getOtherName() != null ? String.join(", ", detailPlant.getOtherName()) : null
        );
        this.setFamily(detailPlant.getFamily());
        this.setWatering(detailPlant.getWatering());
        this.setLightExposure(
                detailPlant.getSunlight() != null ? String.join(", ", detailPlant.getSunlight()) : null
        );
        this.setSoil(
                detailPlant.getSoil() != null ? String.join(", ", detailPlant.getSoil()) : null
        );
        this.setGrowthRate(detailPlant.getGrowthRate());
        this.setCareLevel(detailPlant.getCareLevel());
        this.setPoisonousToPet(
                (detailPlant.isPoisonousToPets())
        );
        this.setDescription(detailPlant.getDescription());
        this.setImageUrl(detailPlant.getDefaultImage() != null ? detailPlant.getDefaultImage().getOriginalUrl() : "");

        for (CareGuideApiItem item : careGuide.getData()) {
            for (CareGuideApiDescription careDescription : item.getData()) {
                switch (careDescription.getType()) {
                    case "watering":
                        this.setWateringDetails(careDescription.getDescription());
                        break;
                    case "sunlight":
                        this.setSunlightDetails(careDescription.getDescription());
                        break;
                    case "pruning":
                        this.setPruningDetails(careDescription.getDescription());
                        break;
                    default:
                        log.debug("Type de care guide non géré : '{}' pour apiId {}", careDescription.getType(), apiId);
                }
            }
        }
    }

    public void updatePlantFromDto(PlantDtoFromJson dto) {
        this.commonName = emptyStringToNull(dto.getCommonName());
        this.otherName = emptyStringToNull(dto.getOtherName());
        this.scientificName = emptyStringToNull(dto.getScientificName());
        this.family = emptyStringToNull(dto.getFamily());
        this.description = emptyStringToNull(dto.getDescription());
        this.careLevel = emptyStringToNull(dto.getCareLevel());
        this.imageUrl = emptyStringToNull(dto.getImageUrl());
        this.watering = emptyStringToNull(dto.getWatering());
        this.soil = emptyStringToNull(dto.getSoil());
        this.lightExposure = emptyStringToNull(dto.getLightExposure());
        this.growthRate = emptyStringToNull(dto.getGrowthRate());
        this.poisonousToPet = dto.isPoisonousToPet();
        this.wateringDetails = emptyStringToNull(dto.getWateringDetails());
        this.sunlightDetails = emptyStringToNull(dto.getSunlightDetails());
        this.pruningDetails = emptyStringToNull(dto.getPruningDetails());
    }

    private String emptyStringToNull(String value){
        return (value == null || value.isEmpty()) ? null : value;
    }
}
