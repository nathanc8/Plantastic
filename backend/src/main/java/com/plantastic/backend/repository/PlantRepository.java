package com.plantastic.backend.repository;

import com.plantastic.backend.models.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    Optional<Plant> findByApiId(int apiId);
}
