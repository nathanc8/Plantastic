package com.plantastic.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlantDetailResponse {
    private String common_name;
    private List<String> scientific_name;
    private List<String> other_name;
    private String family;
    private String watering;
    private List<String> sunlight;
    private String soil;
    private String growth_rate;
    private String care_level;
    private boolean poisonous_to_pets;
    private String description;
    private Image default_image;
    // getters & setters
}

