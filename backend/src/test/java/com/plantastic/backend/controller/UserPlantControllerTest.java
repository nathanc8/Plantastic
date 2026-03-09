package com.plantastic.backend.controller;

import com.plantastic.backend.dto.plants.CreateUserPlantRequest;
import com.plantastic.backend.dto.plants.UserPlantDetailsDto;
import com.plantastic.backend.dto.plants.UserPlantSummaryDto;
import com.plantastic.backend.models.entity.UserPlant;
import com.plantastic.backend.models.types.UserRole;
import com.plantastic.backend.security.user.CustomUserDetails;
import com.plantastic.backend.service.UserPlantService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserPlantController Tests")
@ActiveProfiles("test")
class UserPlantControllerTest {

  @Mock
  private UserPlantService userPlantService;

  @InjectMocks
  private UserPlantController userPlantController;

  private CustomUserDetails currentUser;
  private CreateUserPlantRequest createUserPlantRequest;
  private UserPlant userPlant;
  private UserPlantDetailsDto userPlantDetailsDto;
  private UserPlantSummaryDto userPlantSummaryDto;

  @BeforeEach
  void setUp() {
    currentUser = new CustomUserDetails(1L, "test@example.com", "testuser", "password123", UserRole.ROLE_USER);

    createUserPlantRequest = new CreateUserPlantRequest();
    createUserPlantRequest.setPlantId(1L);
    createUserPlantRequest.setNickname("My Plant");
    createUserPlantRequest.setAcquisitionDate(LocalDate.now().minusDays(10));
    createUserPlantRequest.setLastWatering(LocalDate.now().minusDays(2));

    userPlant = new UserPlant();
    userPlant.setId(1L);
    userPlant.setNickname("My Plant");

    userPlantDetailsDto = new UserPlantDetailsDto(
      1L,
      "My Plant",
      "Monstera Deliciosa",
      LocalDate.now().minusDays(10),
      "Monstera deliciosa",
      LocalDate.now().minusDays(2),
      LocalDate.now().plusDays(5),
      7,
      "https://example.com/plant.jpg",
      "https://example.com/user-plant.jpg",
      "BRIGHT_INDIRECT"
    );

    userPlantSummaryDto = new UserPlantSummaryDto(
      1L,
      "My Plant",
      "Monstera Deliciosa",
      LocalDate.now().minusDays(2),
      LocalDate.now().plusDays(5),
      "https://example.com/plant.jpg",
      "https://example.com/user-plant.jpg"
    );
  }

  @Nested
  @DisplayName("createOneUserPlant Tests")
  class CreateOneUserPlantTests {

    @Test
    @DisplayName("Should create user plant successfully with file")
    void shouldCreateUserPlantWithFile() throws IOException {
      // Arrange
      MultipartFile file = new MockMultipartFile(
        "file",
        "plant.jpg",
        "image/jpeg",
        "image content".getBytes()
      );

      when(userPlantService.createOneUserPlant(createUserPlantRequest, file, currentUser))
        .thenReturn(userPlant);

      // Act
      ResponseEntity<String> response = userPlantController.createOneUserPlant(
        createUserPlantRequest,
        file,
        currentUser
      );

      // Assert
      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      assertTrue(response.getBody().contains("UserPlant successfully created: 1"));
      verify(userPlantService, times(1)).createOneUserPlant(createUserPlantRequest, file, currentUser);
    }

    @Test
    @DisplayName("Should create user plant successfully without file")
    void shouldCreateUserPlantWithoutFile() throws IOException {
      // Arrange
      when(userPlantService.createOneUserPlant(createUserPlantRequest, null, currentUser))
        .thenReturn(userPlant);

      // Act
      ResponseEntity<String> response = userPlantController.createOneUserPlant(
        createUserPlantRequest,
        null,
        currentUser
      );

      // Assert
      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      assertTrue(response.getBody().contains("UserPlant successfully created: 1"));
      verify(userPlantService, times(1)).createOneUserPlant(createUserPlantRequest, null, currentUser);
    }

    @Test
    @DisplayName("Should create user plant with different user")
    void shouldCreateUserPlantWithDifferentUser() throws IOException {
      // Arrange
      CustomUserDetails anotherUser = new CustomUserDetails(
        2L,
        "another@example.com",
        "anotheruser",
        "password456",
        UserRole.ROLE_USER
      );

      when(userPlantService.createOneUserPlant(createUserPlantRequest, null, anotherUser))
        .thenReturn(userPlant);

      // Act
      ResponseEntity<String> response = userPlantController.createOneUserPlant(
        createUserPlantRequest,
        null,
        anotherUser
      );

      // Assert
      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      verify(userPlantService, times(1)).createOneUserPlant(createUserPlantRequest, null, anotherUser);
    }
  }

  @Nested
  @DisplayName("getUserPlantDetailsById Tests")
  class GetUserPlantDetailsByIdTests {

    @Test
    @DisplayName("Should retrieve user plant details successfully")
    void shouldGetUserPlantDetailsSuccessfully() {
      // Arrange
      Long userPlantId = 1L;
      when(userPlantService.getUserPlantDetailsById(userPlantId))
        .thenReturn(userPlantDetailsDto);

      // Act
      ResponseEntity<UserPlantDetailsDto> response = userPlantController.getUserPlantDetailsById(userPlantId);

      // Assert
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody());
      assertEquals("My Plant", response.getBody().nickname());
      assertEquals("Monstera Deliciosa", response.getBody().commonName());
      assertEquals(7, response.getBody().waterFreq());
      verify(userPlantService, times(1)).getUserPlantDetailsById(userPlantId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when plant not found")
    void shouldThrowEntityNotFoundExceptionWhenPlantNotFound() {
      // Arrange
      Long userPlantId = 999L;
      when(userPlantService.getUserPlantDetailsById(userPlantId))
        .thenThrow(new EntityNotFoundException("Plant not found"));

      // Act & Assert
      assertThrows(EntityNotFoundException.class, () ->
        userPlantController.getUserPlantDetailsById(userPlantId)
      );
      verify(userPlantService, times(1)).getUserPlantDetailsById(userPlantId);
    }

    @Test
    @DisplayName("Should retrieve user plant details with correct dates")
    void shouldRetrievePlantDetailsWithCorrectDates() {
      // Arrange
      Long userPlantId = 1L;
      when(userPlantService.getUserPlantDetailsById(userPlantId))
        .thenReturn(userPlantDetailsDto);

      // Act
      ResponseEntity<UserPlantDetailsDto> response = userPlantController.getUserPlantDetailsById(userPlantId);

      // Assert
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(LocalDate.now().minusDays(2), response.getBody().lastWatering());
      assertEquals(LocalDate.now().plusDays(5), response.getBody().nextWatering());
    }
  }

  @Nested
  @DisplayName("waterUserPlantbyId Tests")
  class WaterUserPlantbyIdTests {

    @Test
    @DisplayName("Should water single user plant successfully")
    void shouldWaterSingleUserPlantSuccessfully() {
      // Arrange
      Long userPlantId = 1L;
      LocalDate wateringDate = LocalDate.now();

      when(userPlantService.updateWateringDaysForOneUserPlant(userPlantId, wateringDate))
        .thenReturn(userPlantDetailsDto);

      // Act
      ResponseEntity<UserPlantDetailsDto> response = userPlantController.waterUserPlantbyId(
        userPlantId,
        wateringDate
      );

      // Assert
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody());
      assertEquals("My Plant", response.getBody().nickname());
      verify(userPlantService, times(1))
        .updateWateringDaysForOneUserPlant(userPlantId, wateringDate);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when plant not found")
    void shouldThrowExceptionWhenPlantNotFound() {
      // Arrange
      Long userPlantId = 999L;
      LocalDate wateringDate = LocalDate.now();

      when(userPlantService.updateWateringDaysForOneUserPlant(userPlantId, wateringDate))
        .thenThrow(new EntityNotFoundException("Plant not found"));

      // Act & Assert
      assertThrows(EntityNotFoundException.class, () ->
        userPlantController.waterUserPlantbyId(userPlantId, wateringDate)
      );
    }

    @Test
    @DisplayName("Should water plant with past watering date")
    void shouldWaterPlantWithPastWateringDate() {
      // Arrange
      Long userPlantId = 1L;
      LocalDate wateringDate = LocalDate.now().minusDays(5);

      when(userPlantService.updateWateringDaysForOneUserPlant(userPlantId, wateringDate))
        .thenReturn(userPlantDetailsDto);

      // Act
      ResponseEntity<UserPlantDetailsDto> response = userPlantController.waterUserPlantbyId(
        userPlantId,
        wateringDate
      );

      // Assert
      assertEquals(HttpStatus.OK, response.getStatusCode());
      verify(userPlantService, times(1))
        .updateWateringDaysForOneUserPlant(userPlantId, wateringDate);
    }
  }

  @Nested
  @DisplayName("waterMultiplesUserPlantByIds Tests")
  class WaterMultiplesUserPlantByIdsTests {

    @Test
    @DisplayName("Should water multiple user plants successfully")
    void shouldWaterMultiplePlantsSuccessfully() {
      // Arrange
      List<Long> userPlantIds = List.of(1L, 2L, 3L);
      LocalDate wateringDate = LocalDate.now();
      List<UserPlantSummaryDto> summaries = List.of(
        userPlantSummaryDto,
        new UserPlantSummaryDto(2L, "Plant 2", "Pothos", LocalDate.now().minusDays(2),
          LocalDate.now().plusDays(5), "url", "user-url"),
        new UserPlantSummaryDto(3L, "Plant 3", "Philodendron", LocalDate.now().minusDays(2),
          LocalDate.now().plusDays(5), "url", "user-url")
      );

      when(userPlantService.updateWateringDaysForMultiplesUserPlants(userPlantIds, wateringDate))
        .thenReturn(summaries);

      // Act
      ResponseEntity<List<UserPlantSummaryDto>> response = userPlantController.waterMultiplesUserPlantByIds(
        userPlantIds,
        wateringDate
      );

      // Assert
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody());
      assertEquals(3, response.getBody().size());
      verify(userPlantService, times(1))
        .updateWateringDaysForMultiplesUserPlants(userPlantIds, wateringDate);
    }

    @Test
    @DisplayName("Should water empty list of plants")
    void shouldHandleEmptyListOfPlants() {
      // Arrange
      List<Long> userPlantIds = List.of();
      LocalDate wateringDate = LocalDate.now();

      when(userPlantService.updateWateringDaysForMultiplesUserPlants(userPlantIds, wateringDate))
        .thenReturn(List.of());

      // Act
      ResponseEntity<List<UserPlantSummaryDto>> response = userPlantController.waterMultiplesUserPlantByIds(
        userPlantIds,
        wateringDate
      );

      // Assert
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody());
      assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Should water single plant in list")
    void shouldWaterSinglePlantInList() {
      // Arrange
      List<Long> userPlantIds = List.of(1L);
      LocalDate wateringDate = LocalDate.now();

      when(userPlantService.updateWateringDaysForMultiplesUserPlants(userPlantIds, wateringDate))
        .thenReturn(List.of(userPlantSummaryDto));

      // Act
      ResponseEntity<List<UserPlantSummaryDto>> response = userPlantController.waterMultiplesUserPlantByIds(
        userPlantIds,
        wateringDate
      );

      // Assert
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(1, response.getBody().size());
      assertEquals("My Plant", response.getBody().get(0).nickname());
    }

    @Test
    @DisplayName("Should throw exception when one plant not found")
    void shouldThrowExceptionWhenOnePlantNotFound() {
      // Arrange
      List<Long> userPlantIds = List.of(1L, 999L);
      LocalDate wateringDate = LocalDate.now();

      when(userPlantService.updateWateringDaysForMultiplesUserPlants(userPlantIds, wateringDate))
        .thenThrow(new EntityNotFoundException("One or more plants not found"));

      // Act & Assert
      assertThrows(EntityNotFoundException.class, () ->
        userPlantController.waterMultiplesUserPlantByIds(userPlantIds, wateringDate)
      );
    }
  }

  @Nested
  @DisplayName("deleteOneUserPlant Tests")
  class DeleteOneUserPlantTests {

    @Test
    @DisplayName("Should delete user plant successfully")
    void shouldDeleteUserPlantSuccessfully() {
      // Arrange
      Long userPlantId = 1L;
      doNothing().when(userPlantService).deleteUserPlantById(userPlantId);

      // Act
      ResponseEntity<Map<String, String>> response = userPlantController.deleteOneUserPlant(userPlantId);

      // Assert
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody());
      assertEquals("Plant deleted successfully", response.getBody().get("message"));
      assertEquals("true", response.getBody().get("success"));
      verify(userPlantService, times(1)).deleteUserPlantById(userPlantId);
    }

    @Test
    @DisplayName("Should return 404 when plant not found")
    void shouldReturn404WhenPlantNotFound() {
      // Arrange
      Long userPlantId = 999L;
      doThrow(new EntityNotFoundException("Plant with id 999 not found"))
        .when(userPlantService).deleteUserPlantById(userPlantId);

      // Act
      ResponseEntity<Map<String, String>> response = userPlantController.deleteOneUserPlant(userPlantId);

      // Assert
      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
      assertNotNull(response.getBody());
      assertEquals("false", response.getBody().get("success"));
      assertTrue(response.getBody().get("message").contains("Plant with id 999 not found"));
      verify(userPlantService, times(1)).deleteUserPlantById(userPlantId);
    }

    @Test
    @DisplayName("Should return 500 on unexpected error")
    void shouldReturn500OnUnexpectedError() {
      // Arrange
      Long userPlantId = 1L;
      doThrow(new RuntimeException("Database connection failed"))
        .when(userPlantService).deleteUserPlantById(userPlantId);

      // Act
      ResponseEntity<Map<String, String>> response = userPlantController.deleteOneUserPlant(userPlantId);

      // Assert
      assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
      assertNotNull(response.getBody());
      assertEquals("false", response.getBody().get("success"));
      assertEquals("An unexpected error occurred", response.getBody().get("message"));
      verify(userPlantService, times(1)).deleteUserPlantById(userPlantId);
    }

    @Test
    @DisplayName("Should handle NullPointerException gracefully")
    void shouldHandleNullPointerException() {
      // Arrange
      Long userPlantId = 1L;
      doThrow(new NullPointerException("Null value encountered"))
        .when(userPlantService).deleteUserPlantById(userPlantId);

      // Act
      ResponseEntity<Map<String, String>> response = userPlantController.deleteOneUserPlant(userPlantId);

      // Assert
      assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
      assertEquals("false", response.getBody().get("success"));
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException gracefully")
    void shouldHandleIllegalArgumentException() {
      // Arrange
      Long userPlantId = -1L;
      doThrow(new IllegalArgumentException("Invalid plant id"))
        .when(userPlantService).deleteUserPlantById(userPlantId);

      // Act
      ResponseEntity<Map<String, String>> response = userPlantController.deleteOneUserPlant(userPlantId);

      // Assert
      assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
      assertEquals("false", response.getBody().get("success"));
    }
  }
}