package com.plantastic.backend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import com.plantastic.backend.dto.plants.UserPlantDetailsDto;
import com.plantastic.backend.dto.plants.UserPlantSummaryDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class UserPlantMapperTest {

  private final UserPlantMapper mapper = new UserPlantMapper();

  @Test
  void toSummary_should_map_all_fields() {
    // given
    LocalDate lastWatering = LocalDate.of(2024, 1, 10);
    LocalDate nextWatering = LocalDate.of(2024, 1, 15);

    UserPlantDetailsDto details = new UserPlantDetailsDto(
      1L,
      "My Ficus",
      "Ficus",
      LocalDate.of(2023, 5, 1),
      "Ficus elastica",
      lastWatering,
      nextWatering,
      7,
      "plant-url",
      "user-plant-url",
      "bright"
    );

    // when
    UserPlantSummaryDto summary = mapper.toSummary(details);

    // then
    assertEquals(details.id(), summary.id());
    assertEquals(details.nickname(), summary.nickname());
    assertEquals(details.commonName(), summary.commonName());
    assertEquals(details.lastWatering(), summary.lastWatering());
    assertEquals(details.nextWatering(), summary.nextWatering());
    assertEquals(details.plantImageUrl(), summary.plantImageUrl());
    assertEquals(details.userPlantImageUrl(), summary.userPlantImageUrl());
  }
}

