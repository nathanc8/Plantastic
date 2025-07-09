package com.plantastic.backend.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantDtoFromJson {
    @JsonProperty("api_id")
    private long apiId;
    @JsonProperty("common_name")
    private String commonName;
    @JsonProperty("other_name")
    private String otherName;
    @JsonProperty("scientific_name")
    private String scientificName;
    private String family;
    private String description;
    @JsonProperty("care_level")
    private String careLevel;
    @JsonProperty("image_url")
    private String imageUrl;
    private String watering;
    private String soil;
    @JsonProperty("light_exposure")
    private String lightExposure;
    @JsonProperty("growth_rate")
    private String growthRate;
    @JsonProperty("watering_details")
    private String wateringDetails;
    @JsonProperty("sunlight_details")
    private String sunlightDetails;
    @JsonProperty("pruning_details")
    private String pruningDetails;

    //On ignore la propriété du JSON, car on a une string à l'intérieur, et on la transforme en booleen à l'aide du setter
    @JsonIgnore
    private boolean poisonousToPet;

    @JsonProperty("poisonous_to_pet")
    public void setPoisonousToPet(String value) {
        this.poisonousToPet = "1".equals(value);
    }

    @Override
    public String toString() {
        return "Plant{" +
                "apiId=" + apiId +
                ", commonName='" + commonName + '\'' +
                ", otherName='" + otherName + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", family='" + family + '\'' +
                ", description='" + description + '\'' +
                ", careLevel='" + careLevel + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", watering='" + watering + '\'' +
                ", soil='" + soil + '\'' +
                ", lightExposure='" + lightExposure + '\'' +
                ", growthRate='" + growthRate + '\'' +
                ", poisonousToPet=" + poisonousToPet +
                ", wateringDetails='" + wateringDetails + '\'' +
                ", sunlightDetails='" + sunlightDetails + '\'' +
                ", pruningDetails='" + pruningDetails + '\'' +
                '}';
    }
}
