package com.plantastic.backend.dto.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantastic.backend.models.entity.Plant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlantDtoFromJson {
    private long apiId;
    private String commonName;
    private String otherName;
    private String scientificName;
    private String family;
    private String description;
    private String careLevel;
    private String imageUrl;
    private String watering;
    private String soil;
    private String lightExposure;
    private String growthRate;
    private boolean poisonousToPet;
    private String wateringDetails;
    private String sunlightDetails;
    private String pruningDetails;


}
