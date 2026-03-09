package com.plantastic.backend.repository;

import com.plantastic.backend.dto.plants.PlantEncyclopediaDetailsDto;
import com.plantastic.backend.dto.plants.PlantEncyclopediaSummaryDto;
import com.plantastic.backend.dto.plants.PlantSummaryDto;

import com.plantastic.backend.models.entity.Plant;
import com.plantastic.backend.models.types.WateringFrequency;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan("com.plantastic.backend.models")
@ActiveProfiles("test")
class PlantRepositoryTest {

  @Autowired
  private PlantRepository plantRepository;

  @Autowired
  private EntityManager entityManager;

  // --------------------------------------------------
  // findByApiId
  // --------------------------------------------------

  @Test
  @DisplayName("findByApiId should return plant when apiId exists")
  void findByApiId_success() {
    Plant plant = createPlant(123L, WateringFrequency.FREQUENT, true);
    entityManager.persist(plant);
    entityManager.flush();

    Optional<Plant> result = plantRepository.findByApiId(123L);

    assertThat(result).isPresent();
    assertThat(result.get().getCommonName()).isEqualTo("Rose");
  }

  @Test
  @DisplayName("findByApiId should return empty when apiId does not exist")
  void findByApiId_notFound() {
    Optional<Plant> result = plantRepository.findByApiId(999L);
    assertThat(result).isEmpty();
  }

  // --------------------------------------------------
  // findAllPlantsSummaries
  // --------------------------------------------------

  @Test
  @DisplayName("findAllPlantsSummaries should return summaries only")
  void findAllPlantsSummaries_success() {
    entityManager.persist(createPlant(1L, WateringFrequency.FREQUENT, false));
    entityManager.persist(createPlant(2L, WateringFrequency.MINIMUM, false));
    entityManager.flush();

    List<PlantSummaryDto> results = plantRepository.findAllPlantsSummaries();

    assertThat(results).hasSize(2);
    assertThat(results.get(0).commonName()).isEqualTo("Rose");
    assertThat(results.get(0).scientificName()).isEqualTo("Rosa");
    assertThat(results.get(0).imageUrl()).isEqualTo("image.jpg");
  }

  // --------------------------------------------------
  // findAllPlantsForEncyclopedia
  // --------------------------------------------------

  @Test
  @DisplayName("findAllPlantsForEncyclopedia should map watering enum to readable text")
  void findAllPlantsForEncyclopedia_caseWhen() {
    entityManager.persist(createPlant(1L, WateringFrequency.FREQUENT, false));
    entityManager.persist(createPlant(2L, WateringFrequency.MINIMUM, false));
    entityManager.flush();

    List<PlantEncyclopediaSummaryDto> results =
      plantRepository.findAllPlantsForEncyclopedia();

    assertThat(results).hasSize(2);
    assertThat(results.get(0).watering())
      .isIn("Frequent (5 days)", "Minimum (20 days)");
  }

  // --------------------------------------------------
  // findPlantEncyclopediaDetails
  // --------------------------------------------------

  @Test
  @DisplayName("findPlantEncyclopediaDetails should return full details with CASE mapping")
  void findPlantEncyclopediaDetails_success() {
    Plant plant = createPlant(1L, WateringFrequency.FREQUENT, true);
    entityManager.persist(plant);
    entityManager.flush();

    PlantEncyclopediaDetailsDto dto =
      plantRepository.findPlantEncyclopediaDetails(plant.getId());

    assertThat(dto).isNotNull();
    assertThat(dto.commonName()).isEqualTo("Rose");
    assertThat(dto.poisonousToPet()).isEqualTo("Toxic for pets");
    assertThat(dto.watering()).isEqualTo("Frequent (5 days)");
  }

  // --------------------------------------------------
  // Utils
  // --------------------------------------------------

  private Plant createPlant(Long apiId, WateringFrequency watering, boolean toxic) {
    Plant plant = new Plant();
    plant.setApiId(apiId);
    plant.setCommonName("Rose");
    plant.setOtherName("Red rose");
    plant.setScientificName("Rosa");
    plant.setFamily("Rosaceae");
    plant.setDescription("A beautiful flower");
    plant.setCareLevel("EASY");
    plant.setImageUrl("image.jpg");
    plant.setSoil("Well drained");
    plant.setLightExposure("SUN");
    plant.setGrowthRate("MEDIUM");
    plant.setWatering(String.valueOf(watering));
    plant.setWateringDetails("Once a week");
    plant.setSunlightDetails("Full sun");
    plant.setPruningDetails("Spring");
    plant.setPoisonousToPet(toxic);
    return plant;
  }
}
