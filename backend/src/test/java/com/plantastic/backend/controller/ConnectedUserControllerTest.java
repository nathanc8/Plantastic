package com.plantastic.backend.controller;

import com.plantastic.backend.dto.auth.AuthUserDto;
import com.plantastic.backend.dto.auth.DigitalGardenResponseDto;
import com.plantastic.backend.dto.plants.UserPlantSummaryDto;
import com.plantastic.backend.models.types.UserRole;
import com.plantastic.backend.security.user.CustomUserDetails;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.plantastic.backend.repository.UserPlantRepository;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ConnectedUserControllerTest {

  @InjectMocks
  private ConnectedUserController connectedUserController;
  @Mock
  private UserPlantRepository userPlantRepository;
  
  private CustomUserDetails validUserDetails;
  private static final Long USER_ID = 1L;
  private static final String USER_EMAIL = "user@example.com";
  private static final String USERNAME = "user";
  private static final String PASSWORD = "password";
  private static final UserRole USER_ROLE = UserRole.ROLE_USER;

  @BeforeEach
  void setUp() {
    validUserDetails = new CustomUserDetails(
      USER_ID,
      USER_EMAIL,
      USERNAME,
      PASSWORD,
      USER_ROLE
    );
  }

  @Test
  @DisplayName("Must return auth infos with a valid user")
  void testGetAuthInformations_WithValidUser_ReturnsOkWithUserData() {
    // When
    ResponseEntity<AuthUserDto> response = connectedUserController.getAuthInformations(validUserDetails);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().id());
    assertEquals("user@example.com", response.getBody().email());
    assertEquals(UserRole.ROLE_USER, response.getBody().role());
  }

  @Test
  @DisplayName("Must return 401 UNAUTHORIZED when userDetails is null")
  void testGetAuthInformations_WithNullUserDetails_ReturnsUnauthorized() {
    // When
    ResponseEntity<AuthUserDto> response = connectedUserController.getAuthInformations(null);

    // Then
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  @DisplayName("Must create authUserDto with corrects values")
  void getAuthInformations_returnsMappedAuthUserDto() {
    // When
    ResponseEntity<AuthUserDto> response =
      connectedUserController.getAuthInformations(validUserDetails);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    AuthUserDto dto = response.getBody();
    assertAll(
      () -> assertEquals(validUserDetails.getId(), dto.id()),
      () -> assertEquals(validUserDetails.getEmail(), dto.email()),
      () -> assertEquals(validUserDetails.getRole(), dto.role())
    );
  }

  @Test
  @DisplayName("Must return a empty response with UNAUTHORIZED status")
  void testGetAuthInformations_UnauthorizedResponseHasNoBody() {
    // When
    ResponseEntity<AuthUserDto> response = connectedUserController.getAuthInformations(null);

    // Then
    assertTrue(response.getStatusCode().is4xxClientError());
    assertFalse(response.hasBody());
  }

  @Test
  @DisplayName("Must return OK status with digital garden containing plants")
  void testGetMyDigitalGarden_WithPlants_ReturnsOkWithPlants() {
    // Arrange
    List<UserPlantSummaryDto> plants = createMockPlants(3);
    when(userPlantRepository.findDigitalGardenByUserId(USER_ID))
      .thenReturn(plants);

    // Act
    ResponseEntity<DigitalGardenResponseDto> response =
      connectedUserController.getMyDigitalGarden(validUserDetails);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    DigitalGardenResponseDto body = response.getBody();
    assertEquals(3, body.getDigitalGarden().size());
    assertEquals(plants, body.getDigitalGarden());

    verify(userPlantRepository, times(1)).findDigitalGardenByUserId(USER_ID);
  }

  @Test
  @DisplayName("Must return OK status with empty digital garden when user has no plants")
  void testGetMyDigitalGarden_EmptyGarden_ReturnsOkWithEmptyList() {
    // Arrange
    when(userPlantRepository.findDigitalGardenByUserId(USER_ID))
      .thenReturn(Collections.emptyList());

    // Act
    ResponseEntity<DigitalGardenResponseDto> response =
      connectedUserController.getMyDigitalGarden(validUserDetails);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getDigitalGarden().isEmpty());

    verify(userPlantRepository, times(1)).findDigitalGardenByUserId(USER_ID);
  }

  @Test
  @DisplayName("Must map AuthUserDto correctly with user details")
  void testGetMyDigitalGarden_MapsAuthUserDtoCorrectly() {
    // Arrange
    List<UserPlantSummaryDto> plants = createMockPlants(1);
    when(userPlantRepository.findDigitalGardenByUserId(USER_ID))
      .thenReturn(plants);

    // Act
    ResponseEntity<DigitalGardenResponseDto> response =
      connectedUserController.getMyDigitalGarden(validUserDetails);

    // Assert
    assertNotNull(response.getBody());
    AuthUserDto authUserDto = response.getBody().getUser();

    assertAll(
      () -> assertEquals(USER_ID, authUserDto.id()),
      () -> assertEquals(USER_EMAIL, authUserDto.email()),
      () -> assertEquals(USER_ROLE, authUserDto.role())
    );
  }

  @Test
  @DisplayName("Must return response body with user data")
  void testGetMyDigitalGarden_ResponseHasValidUserData() {
    // Arrange
    when(userPlantRepository.findDigitalGardenByUserId(USER_ID))
      .thenReturn(Collections.emptyList());

    // Act
    ResponseEntity<DigitalGardenResponseDto> response =
      connectedUserController.getMyDigitalGarden(validUserDetails);

    // Assert
    assertTrue(response.hasBody());
    assertNotNull(response.getBody().getUser());
    assertEquals(validUserDetails.getId(), response.getBody().getUser().id());
    assertEquals(validUserDetails.getEmail(), response.getBody().getUser().email());
    assertEquals(validUserDetails.getRole(), response.getBody().getUser().role());
  }

  @Test
  @DisplayName("Must call repository with correct user ID")
  void testGetMyDigitalGarden_CallsRepositoryWithCorrectUserId() {
    // Arrange
    when(userPlantRepository.findDigitalGardenByUserId(USER_ID))
      .thenReturn(Collections.emptyList());

    // Act
    connectedUserController.getMyDigitalGarden(validUserDetails);

    // Assert
    verify(userPlantRepository, times(1)).findDigitalGardenByUserId(USER_ID);
    verifyNoMoreInteractions(userPlantRepository);
  }

  @Test
  @DisplayName("Must return multiple plants with correct data")
  void testGetMyDigitalGarden_ReturnMultiplePlants_AllDataIntact() {
    // Arrange
    List<UserPlantSummaryDto> plants = createMockPlants(5);
    when(userPlantRepository.findDigitalGardenByUserId(USER_ID))
      .thenReturn(plants);

    // Act
    ResponseEntity<DigitalGardenResponseDto> response =
      connectedUserController.getMyDigitalGarden(validUserDetails);

    // Assert
    assertEquals(5, response.getBody().getDigitalGarden().size());

    for (int i = 0; i < 5; i++) {
      UserPlantSummaryDto plant = response.getBody().getDigitalGarden().get(i);
      assertEquals((long) (i + 1), plant.id());
      assertNotNull(plant.nickname());
      assertNotNull(plant.commonName());
    }
  }

  @Test
  @DisplayName("Must return correct DTO structure with all fields populated")
  void testGetMyDigitalGarden_ResponseDtoStructureIsValid() {
    // Arrange
    List<UserPlantSummaryDto> plants = createMockPlants(1);
    when(userPlantRepository.findDigitalGardenByUserId(USER_ID))
      .thenReturn(plants);

    // Act
    ResponseEntity<DigitalGardenResponseDto> response =
      connectedUserController.getMyDigitalGarden(validUserDetails);

    // Assert
    DigitalGardenResponseDto dto = response.getBody();
    assertNotNull(dto.getUser());
    assertNotNull(dto.getDigitalGarden());
    assertFalse(dto.getDigitalGarden().isEmpty());
  }

  @Test
  @DisplayName("Must handle single plant correctly")
  void testGetMyDigitalGarden_WithSinglePlant_ReturnsCorrectly() {
    // Arrange
    List<UserPlantSummaryDto> plants = createMockPlants(1);
    when(userPlantRepository.findDigitalGardenByUserId(USER_ID))
      .thenReturn(plants);

    // Act
    ResponseEntity<DigitalGardenResponseDto> response =
      connectedUserController.getMyDigitalGarden(validUserDetails);

    // Assert
    assertEquals(1, response.getBody().getDigitalGarden().size());
    assertEquals(1L, response.getBody().getDigitalGarden().get(0).id());
  }

  @Test
  @DisplayName("Must not call repository more than once per request")
  void testGetMyDigitalGarden_CallsRepositoryOnlyOnce() {
    // Arrange
    when(userPlantRepository.findDigitalGardenByUserId(USER_ID))
      .thenReturn(Collections.emptyList());

    // Act
    connectedUserController.getMyDigitalGarden(validUserDetails);

    // Assert
    verify(userPlantRepository, times(1)).findDigitalGardenByUserId(anyLong());
  }

  @Test
  @DisplayName("Must preserve plant data order from repository")
  void testGetMyDigitalGarden_PreservesPlantOrderFromRepository() {
    // Arrange
    List<UserPlantSummaryDto> plants = createMockPlantsWithCustomIds(new Long[]{3L, 1L, 2L});
    when(userPlantRepository.findDigitalGardenByUserId(USER_ID))
      .thenReturn(plants);

    // Act
    ResponseEntity<DigitalGardenResponseDto> response =
      connectedUserController.getMyDigitalGarden(validUserDetails);

    // Assert
    List<UserPlantSummaryDto> returnedPlants = response.getBody().getDigitalGarden();
    assertEquals(3L, returnedPlants.get(0).id());
    assertEquals(1L, returnedPlants.get(1).id());
    assertEquals(2L, returnedPlants.get(2).id());
  }

  // Helper methods

  private List<UserPlantSummaryDto> createMockPlants(int count) {
    List<UserPlantSummaryDto> plants = new ArrayList<>();
    LocalDate today = LocalDate.now();

    for (int i = 0; i < count; i++) {
      plants.add(new UserPlantSummaryDto(
        (long) (i + 1),
        "Plant " + (i + 1),
        "Common Name " + (i + 1),
        today.minusDays(3),
        today.plusDays(4),
        "plant-image-" + (i + 1) + ".jpg",
        "user-plant-image-" + (i + 1) + ".jpg"
      ));
    }

    return plants;
  }

  private List<UserPlantSummaryDto> createMockPlantsWithCustomIds(Long[] ids) {
    List<UserPlantSummaryDto> plants = new ArrayList<>();
    LocalDate today = LocalDate.now();
    int index = 0;

    for (Long id : ids) {
      plants.add(new UserPlantSummaryDto(
        id,
        "Plant " + id,
        "Common Name " + id,
        today.minusDays(3),
        today.plusDays(4),
        "plant-image-" + id + ".jpg",
        "user-plant-image-" + id + ".jpg"
      ));
      index++;
    }

    return plants;
  }
}
