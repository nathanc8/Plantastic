package com.plantastic.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "plants")
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long apiId;
    @Column (nullable = false)
    private String commonName;
    @Column
    private String otherName;
    @Column (nullable = false)
    private String scientificName;
    @Column (nullable = false)
    private String family;
    @Column (nullable = false)
    private String description;
    @Column
    private String careLevel;
    @Column (nullable = false)
    private String imageUrl;
    @Column (nullable = false)
    private String watering;
    @Column (nullable = false)
    private String repottingMethod;
    @Column (nullable = false)
    private String soil;
    @Column (nullable = false)
    private LightExposure lightExposure;
    @Column
    private GrowthRate growthRate;
    @Column
    private boolean poisonousToPet;
    @Column
    private String wateringDetails;
    @Column
    private String sunlightDetails;
    @Column
    private String pruningDetails;

}
