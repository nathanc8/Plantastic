package com.plantastic.backend.models.entity;

import com.plantastic.backend.initdb.dto.api.*;
import com.plantastic.backend.initdb.dto.json.PlantDtoFromJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
    @Column(name = "common_name")
    private String commonName;
    @Column(name = "other_name")
    private String otherName;
    @Column(name = "scientific_name")
    private String scientificName;
    @Column
    private String family;
    @Column(length = 2000)
    private String description;
    @Column(name = "care_level")
    private String careLevel;
    @Column
    private String imageUrl;
    @Column
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
        this.commonName = emptyStringToNull(detailPlant.getCommonName());
        this.scientificName = emptyStringToNull(String.join(",",detailPlant.getScientificName()));
        this.otherName = emptyStringToNull(String.join(",",detailPlant.getOtherName()));
        this.family = emptyStringToNull(detailPlant.getFamily());
        this.watering = emptyStringToNull(detailPlant.getWatering());
        this.lightExposure = emptyStringToNull(String.join(",",detailPlant.getSunlight()));
        this.soil = emptyStringToNull(String.join(",",detailPlant.getSoil()));
        this.growthRate = emptyStringToNull(detailPlant.getGrowthRate());
        this.careLevel = emptyStringToNull(detailPlant.getCareLevel());
        this.poisonousToPet = detailPlant.isPoisonousToPets();
        this.description = emptyStringToNull(detailPlant.getDescription());

        ImageApi defaultImage = detailPlant.getDefaultImage();
        String originalUrl = (defaultImage!=null) ? defaultImage.getOriginalUrl() : null;
        this.imageUrl = emptyStringToNull(originalUrl);

        for (CareGuideApiItem item : careGuide.getData()) {
            for (CareGuideApiDescription careDescription : item.getData()) {
                switch (careDescription.getType()) {
                    case "watering":
                        this.wateringDetails = emptyStringToNull(careDescription.getDescription());
                        break;
                    case "sunlight":
                        this.sunlightDetails = emptyStringToNull(careDescription.getDescription());
                        break;
                    case "pruning":
                        this.pruningDetails = emptyStringToNull(careDescription.getDescription());
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
