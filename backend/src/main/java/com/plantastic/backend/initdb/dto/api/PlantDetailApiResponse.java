package com.plantastic.backend.initdb.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantDetailApiResponse {
    @JsonProperty("common_name")
    private String commonName;
    @JsonProperty("scientific_name")
    private List<String> scientificName;
    @JsonProperty("other_name")
    private List<String> otherName;
    private String family;
    private String watering;
    private List<String> sunlight;
    private List<String> soil;
    @JsonProperty("growth_rate")
    private String growthRate;
    @JsonProperty("care_level")
    private String careLevel;
    @JsonProperty("poisonous_to_pets")
    private boolean poisonousToPets;
    private String description;
    @JsonProperty("default_image")
    private ImageApi defaultImage;
}

