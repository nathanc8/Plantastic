package com.plantastic.backend.models.entity;

import com.plantastic.backend.models.types.CareType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "care")
public class Care {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column (name = "care_type",nullable = false)
    private CareType careType;

    @Column (name = "care_date",nullable = false)
    private LocalDate careDate;

    @Column
    private String notes;
}
