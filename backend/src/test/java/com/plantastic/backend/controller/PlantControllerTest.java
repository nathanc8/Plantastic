package com.plantastic.backend.controller;

import com.plantastic.backend.dto.plants.CreatePlantRequest;
import com.plantastic.backend.dto.plants.PlantEncyclopediaDetailsDto;
import com.plantastic.backend.dto.plants.PlantEncyclopediaSummaryDto;
import com.plantastic.backend.dto.plants.PlantSummaryDto;
import com.plantastic.backend.models.entity.Plant;
import com.plantastic.backend.service.PlantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlantController - Unit Tests")
@ActiveProfiles("test")
class PlantControllerTest {

  @Mock
  private PlantService plantService;

  @Mock
  private MultipartFile mockFile;

  @InjectMocks
  private PlantController plantController;

  private PlantSummaryDto plantSummaryDto;
  private PlantEncyclopediaSummaryDto plantEncyclopediaSummaryDto;
  private PlantEncyclopediaDetailsDto plantEncyclopediaDetailsDto;
  private Plant plant;
  private CreatePlantRequest createPlantRequest;

  @BeforeEach
  void setUp() {
    plantSummaryDto = new PlantSummaryDto(
      1L,
      "Rose",
      "Rosa damascena",
      "https://example.com/rose.jpg"
    );
    
    plantEncyclopediaSummaryDto = new PlantEncyclopediaSummaryDto(
      1L,
      "Rose",
      "Rosa damascena",
      "https://example.com/rose.jpg",
      "Rosaceae",
      "A beautiful flower",
      "Average"
    );
    
    plantEncyclopediaDetailsDto = new PlantEncyclopediaDetailsDto(
      1L,
      "Rose",
      "Rosa",
      "Rosa damascena",
      "Rosaceae",
      "A beautiful flower",
      "Medium",
      "https://example.com/rose.jpg",
      "",
      "Direct to semi-direct",
      "Fast",
      "Water regularly",
      "Full sun",
      "",
      "No",
      "Average"
    );

    plant = new Plant();
    plant.setId(1L);
    plant.setCommonName("Rose");
    plant.setScientificName("Rosa damascena");
    plant.setImageUrl("https://example.com/rose.jpg");
    plant.setFamily("Rosaceae");
    plant.setDescription("A beautiful flower");
    plant.setCareLevel("Medium");
    plant.setWatering("Average");
    
    createPlantRequest = new CreatePlantRequest();
    createPlantRequest.setCommonName("Rose");
    createPlantRequest.setOtherName("Rosa");
    createPlantRequest.setScientificName("Rosa damascena");
    createPlantRequest.setFamily("Rosaceae");
    createPlantRequest.setDescription("A beautiful flower");
    createPlantRequest.setCareLevel("Medium");
    createPlantRequest.setWatering("Average");
    createPlantRequest.setSoil("");
    createPlantRequest.setLightExposure("Direct to semi-direct");
    createPlantRequest.setGrowthRate("Fast");
    createPlantRequest.setWateringDetails("Water regularly");
    createPlantRequest.setSunlightDetails("Full sun");
    createPlantRequest.setPruningDetails("");
    createPlantRequest.setPoisonousToPet(false);
  }

  // ========== Tests for getAllPlantsSummaries ==========

  @Test
  @DisplayName("getAllPlantsSummaries should return a plant summaries list")
  void testGetAllPlantsSummaries_Success() {
    // Arrange
    List<PlantSummaryDto> expectedPlants = List.of(plantSummaryDto);
    when(plantService.getAllPlantsSummaries()).thenReturn(expectedPlants);

    // Act
    ResponseEntity<List<PlantSummaryDto>> response = plantController.getAllPlantsSummaries();

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("Rose", response.getBody().getFirst().commonName());
    assertEquals("Rosa damascena", response.getBody().getFirst().scientificName());

    // Verify
    verify(plantService, times(1)).getAllPlantsSummaries();
    verifyNoMoreInteractions(plantService);
  }

  @Test
  @DisplayName("getAllPlantsSummaries shoudl return empty list when there is no plants")
  void testGetAllPlantsSummaries_EmptyList() {
    // Arrange
    when(plantService.getAllPlantsSummaries()).thenReturn(new ArrayList<>());

    // Act
    ResponseEntity<List<PlantSummaryDto>> response = plantController.getAllPlantsSummaries();

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());
    assertTrue(response.getBody().isEmpty());

    // Verify
    verify(plantService, times(1)).getAllPlantsSummaries();
  }

  // ========== Tests for getAllPlantsEncyclopedia ==========

  @Test
  @DisplayName("getAllPlantsEncyclopedia should return an empty list if there is no plants")
  void testGetAllPlantsEncyclopedia_EmptyList() {
    // Arrange
    when(plantService.getAllPlantsEncyclopedia()).thenReturn(new ArrayList<>());

    // Act
    ResponseEntity<List<PlantEncyclopediaSummaryDto>> response = plantController.getAllPlantsEncyclopedia();

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());

    // Verify
    verify(plantService, times(1)).getAllPlantsEncyclopedia();
  }

  @Test
  @DisplayName("getAllPlantsEncyclopedia should return the full encyclopedia")
  void testGetAllPlantsEncyclopedia_MultiplePlants() {
    // Arrange
    PlantEncyclopediaSummaryDto plant2 = new PlantEncyclopediaSummaryDto(
      2L,
      "Tulip",
      "Tulipa gesneriana",
      "https://example.com/tulip.jpg",
      "Liliaceae",
      "An elegant flower",
      "Minimum"
    );
    List<PlantEncyclopediaSummaryDto> expectedPlants = List.of(plantEncyclopediaSummaryDto, plant2);
    when(plantService.getAllPlantsEncyclopedia()).thenReturn(expectedPlants);

    // Act
    ResponseEntity<List<PlantEncyclopediaSummaryDto>> response = plantController.getAllPlantsEncyclopedia();

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().size());
    assertEquals("Rose", response.getBody().get(0).commonName());
    assertEquals("Tulip", response.getBody().get(1).commonName());

    // Verify
    verify(plantService, times(1)).getAllPlantsEncyclopedia();
  }

  // ========== Tests for getOnePlantEncyclopediaDetails ==========

  @Test
  @DisplayName("getOnePlantEncyclopediaDetails should return plant details")
  void testGetOnePlantEncyclopediaDetails_Success() {
    // Arrange
    Long plantId = 1L;
    when(plantService.getOnePlantEncylopediaDetails(plantId))
      .thenReturn(plantEncyclopediaDetailsDto);

    // Act
    ResponseEntity<PlantEncyclopediaDetailsDto> response =
      plantController.getOnePlantEncyclopediaDetails(plantId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().id());
    assertEquals("Rose", response.getBody().commonName());
    assertEquals("Rosa damascena", response.getBody().scientificName());
    assertEquals("Rosaceae", response.getBody().family());
    assertEquals("Medium", response.getBody().careLevel());
    assertEquals("No", response.getBody().poisonousToPet());

    // Verify
    verify(plantService, times(1)).getOnePlantEncylopediaDetails(plantId);
  }

  @Test
  @DisplayName("getOnePlantEncyclopediaDetails should throw an exception if there is no plant")
  void testGetOnePlantEncyclopediaDetails_NotFound() {
    // Arrange
    Long plantId = 999L;
    when(plantService.getOnePlantEncylopediaDetails(plantId))
      .thenThrow(new IllegalArgumentException("Plant not found with ID : " + plantId));

    // Act & Assert
    assertThrows(IllegalArgumentException.class,
      () -> plantController.getOnePlantEncyclopediaDetails(plantId));

    // Verify
    verify(plantService, times(1)).getOnePlantEncylopediaDetails(plantId);
  }

  @Test
  @DisplayName("getOnePlantEncyclopediaDetails should accept different IDs")
  void testGetOnePlantEncyclopediaDetails_DifferentIds() {
    // Arrange
    Long plantId = 42L;
    PlantEncyclopediaDetailsDto plant42 = new PlantEncyclopediaDetailsDto(
      42L, "Lys", "Lilium", "Lilium candidum", "Liliaceae",
      "Elegant Plant", "Easy", "https://example.com/lily.jpg",
      "", "Sun", "Medium", "Moderate", "Full sun",
      "", "No", "Average"
    );
    when(plantService.getOnePlantEncylopediaDetails(plantId)).thenReturn(plant42);

    // Act
    ResponseEntity<PlantEncyclopediaDetailsDto> response =
      plantController.getOnePlantEncyclopediaDetails(plantId);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(42L, Objects.requireNonNull(response.getBody()).id());
    assertEquals("Lys", response.getBody().commonName());

    // Verify
    verify(plantService, times(1)).getOnePlantEncylopediaDetails(plantId);
  }

  @Test
  @DisplayName("getOnePlantEncyclopediaDetails should contain every detail of the plant")
  void testGetOnePlantEncyclopediaDetails_AllFields() {
    // Arrange
    Long plantId = 1L;
    when(plantService.getOnePlantEncylopediaDetails(plantId))
      .thenReturn(plantEncyclopediaDetailsDto);

    // Act
    ResponseEntity<PlantEncyclopediaDetailsDto> response =
      plantController.getOnePlantEncyclopediaDetails(plantId);

    // Assert
    assertNotNull(response.getBody());
    PlantEncyclopediaDetailsDto details = response.getBody();

    assertNotNull(details.id());
    assertNotNull(details.commonName());
    assertNotNull(details.otherName());
    assertNotNull(details.scientificName());
    assertNotNull(details.family());
    assertNotNull(details.description());
    assertNotNull(details.careLevel());
    assertNotNull(details.imageUrl());
    assertNotNull(details.soil());
    assertNotNull(details.lightExposure());
    assertNotNull(details.growthRate());
    assertNotNull(details.wateringDetails());
    assertNotNull(details.sunlightDetails());
    assertNotNull(details.pruningDetails());
    assertNotNull(details.poisonousToPet());
    assertNotNull(details.watering());

    // Verify
    verify(plantService, times(1)).getOnePlantEncylopediaDetails(plantId);
  }

  // ========== Tests for addOnePlantToDb ==========

  @Test
  @DisplayName("addOnePlantToDb should add a plant with success (return code 200)")
  void testAddOnePlantToDb_Success() throws IOException {
    // Arrange
    when(plantService.createPlantAndSaveToDb(any(CreatePlantRequest.class), any(MultipartFile.class)))
      .thenReturn(plant);

    // Act
    ResponseEntity<Plant> response = plantController.addOnePlantToDb(createPlantRequest, mockFile);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().getId());
    assertEquals("Rose", response.getBody().getCommonName());
    assertEquals("Rosa damascena", response.getBody().getScientificName());

    // Verify
    verify(plantService, times(1)).createPlantAndSaveToDb(
      any(CreatePlantRequest.class),
      any(MultipartFile.class)
    );
  }

  @Test
  @DisplayName("addOnePlantToDb should manage incorrect requests")
  void testAddOnePlantToDb_InvalidData() throws IOException {
    // Arrange
    CreatePlantRequest invalidRequest = new CreatePlantRequest();
    // commonName is null: invalid data

    when(plantService.createPlantAndSaveToDb(any(CreatePlantRequest.class), any(MultipartFile.class)))
      .thenThrow(new IllegalArgumentException("Données invalides: commonName est requis"));

    // Act & Assert
    assertThrows(IllegalArgumentException.class,
      () -> plantController.addOnePlantToDb(invalidRequest, mockFile));

    // Verify
    verify(plantService, times(1)).createPlantAndSaveToDb(any(), any());
  }

  @Test
  @DisplayName("addOnePlantToDb shoudl return the full plant")
  void testAddOnePlantToDb_ReturnCompletePlant() throws IOException {
    // Arrange
    Plant completePlant = createCompletePlant();

    when(plantService.createPlantAndSaveToDb(any(CreatePlantRequest.class), any(MultipartFile.class)))
      .thenReturn(completePlant);

    // Act
    ResponseEntity<Plant> response = plantController.addOnePlantToDb(createPlantRequest, mockFile);

    // Assert
    assertNotNull(response.getBody());
    Plant returnedPlant = response.getBody();
    assertEquals("Rose", returnedPlant.getCommonName());
    assertEquals("Rosa", returnedPlant.getOtherName());
    assertEquals("Rosa damascena", returnedPlant.getScientificName());
    assertEquals("Rosaceae", returnedPlant.getFamily());
    assertEquals("Medium", returnedPlant.getCareLevel());
    assertFalse(returnedPlant.isPoisonousToPet());

    // Verify
    verify(plantService, times(1)).createPlantAndSaveToDb(any(), any());
  }

  private static Plant createCompletePlant() {
    Plant completePlant = new Plant();
    completePlant.setId(1L);
    completePlant.setCommonName("Rose");
    completePlant.setOtherName("Rosa");
    completePlant.setScientificName("Rosa damascena");
    completePlant.setFamily("Rosaceae");
    completePlant.setDescription("A beautiful flower with a strong perfume");
    completePlant.setCareLevel("Medium");
    completePlant.setWatering("Average");
    completePlant.setSoil("");
    completePlant.setLightExposure("Full sun");
    completePlant.setGrowthRate("Fast");
    completePlant.setWateringDetails("Water regularly");
    completePlant.setSunlightDetails("Full sun");
    completePlant.setPruningDetails("");
    completePlant.setPoisonousToPet(false);
    return completePlant;
  }
}