package com.plantastic.backend.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "plants")
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column (name = "api_id")
    private long apiId;
    @Column (name = "common_name",nullable = false)
    private String commonName;
    @Column(name = "other_name")
    private String otherName;
    @Column (name = "scientific_name",nullable = false)
    private String scientificName;
    @Column (nullable = false)
    private String family;
    @Column (nullable = false)
    private String description;
    @Column (name = "care_level")
    private String careLevel;
    @Column (nullable = false)
    private String imageUrl;
    @Column (nullable = false)
    private String watering;
    @Column (name = "repotting_method", nullable = false)
    private String repottingMethod;
    @Column
    private List<String> soil;
    @Column (name = "light_exposure", nullable = false)
    private List<String> lightExposure;
    @Column (name = "growth_rate")
    private String growthRate;
    @Column (name = "poisonous_to_pet")
    private boolean poisonousToPet;
    @Column(name = "watering_details")
    private String wateringDetails;
    @Column(name = "sunlight_details")
    private String sunlightDetails;
    @Column(name = "pruning_details")
    private String pruningDetails;

}
