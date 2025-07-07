package com.plantastic.backend.dto;

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
public class CareGuideItem {
    @JsonProperty("species_id")
    private String speciesId;
    @JsonProperty("section")
    private List<CareGuideDescription> data;
}