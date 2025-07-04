package com.plantastic.backend.controller;

import com.plantastic.backend.models.entity.Plant;
import com.plantastic.backend.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plants")
public class PlantController {

    @Autowired
    private PlantRepository plantRepository;

    @PostMapping
    public Plant addOnePlant(@RequestBody Plant plant) {
        return plantRepository.save(plant);
    }
}
