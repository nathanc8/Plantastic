package com.plantastic.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantSummary {

    @JsonProperty("id")
    private int apiId;

    @Override
    public String toString() {
        return "PlantSummary{" +
                "apiId='" + apiId + '\'' +
                '}';
    }
}