package com.plantastic.backend.repository;

import com.plantastic.backend.dto.plants.UserPlantDetailsDto;
import com.plantastic.backend.dto.plants.UserPlantSummaryDto;
import com.plantastic.backend.models.entity.Plant;
import com.plantastic.backend.models.entity.User;
import com.plantastic.backend.models.entity.UserPlant;
import com.plantastic.backend.models.types.WateringFrequency;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan("com.plantastic.backend.models")
@ActiveProfiles("test")
class UserPlantRepositoryTest {

  @Autowired
  private UserPlantRepository userPlantRepository;

  @Autowired
  private EntityManager entityManager;

  // --------------------------------------------------
  // findDigitalGardenByUserId
  // --------------------------------------------------

  @Test
  @DisplayName("findDigitalGardenByUserId should return user's plants")
  void findDigitalGardenByUserId_success() {
    User user = createUser();
    Plant plant = createPlant();
    UserPlant userPlant = createUserPlant(user, plant);

    entityManager.persist(user);
    entityManager.persist(plant);
    entityManager.persist(userPlant);
    entityManager.flush();

    List<UserPlantSummaryDto> results =
      userPlantRepository.findDigitalGardenByUserId(user.getId());

    assertThat(results).hasSize(1);

    UserPlantSummaryDto dto = results.get(0);
    assertThat(dto.nickname()).isEqualTo("My Rose");
    assertThat(dto.commonName()).isEqualTo("Rose");
    assertThat(dto.plantImageUrl()).isEqualTo("plant.jpg");
    assertThat(dto.userPlantImageUrl()).isEqualTo("userPlant.jpg");
  }

  @Test
  @DisplayName("findDigitalGardenByUserId should return empty list if user has no plants")
  void findDigitalGardenByUserId_empty() {
    User user = createUser();
    entityManager.persist(user);
    entityManager.flush();

    List<UserPlantSummaryDto> results =
      userPlantRepository.findDigitalGardenByUserId(user.getId());

    assertThat(results).isEmpty();
  }

  // --------------------------------------------------
  // findUserPlantDetailsById
  // --------------------------------------------------

  @Test
  @DisplayName("findUserPlantDetailsById should return full details")
  void findUserPlantDetailsById_success() {
    User user = createUser();
    Plant plant = createPlant();
    UserPlant userPlant = createUserPlant(user, plant);

    entityManager.persist(user);
    entityManager.persist(plant);
    entityManager.persist(userPlant);
    entityManager.flush();

    UserPlantDetailsDto dto =
      userPlantRepository.findUserPlantDetailsById(userPlant.getId());

    assertThat(dto).isNotNull();
    assertThat(dto.nickname()).isEqualTo("My Rose");
    assertThat(dto.commonName()).isEqualTo("Rose");
    assertThat(dto.scientificName()).isEqualTo("Rosa");
    assertThat(dto.userPlantImageUrl()).isEqualTo("userPlant.jpg");
  }

  // --------------------------------------------------
  // Utils
  // --------------------------------------------------

  private User createUser() {
    User user = new User();
    user.setUsername("user");
    user.setEmail("user@test.com");
    user.setPassword("password");
    return user;
  }

  private Plant createPlant() {
    Plant plant = new Plant();
    plant.setCommonName("Rose");
    plant.setScientificName("Rosa");
    plant.setImageUrl("plant.jpg");
    plant.setWatering(String.valueOf(WateringFrequency.FREQUENT));
    return plant;
  }

  private UserPlant createUserPlant(User user, Plant plant) {
    UserPlant userPlant = new UserPlant();
    userPlant.setUser(user);
    userPlant.setPlant(plant);
    userPlant.setNickname("My Rose");
    userPlant.setAcquisitionDate(LocalDate.now().minusDays(10));
    userPlant.setLastWatering(LocalDate.now().minusDays(2));
    userPlant.setNextWatering();
    userPlant.setImageUrl("userPlant.jpg");
    return userPlant;
  }
}
