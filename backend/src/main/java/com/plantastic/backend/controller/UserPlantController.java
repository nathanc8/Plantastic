package com.plantastic.backend.controller;

import com.plantastic.backend.dto.plants.CreateUserPlantRequest;
import com.plantastic.backend.dto.plants.UserPlantDetailsDto;
import com.plantastic.backend.dto.plants.UserPlantSummaryDto;
import com.plantastic.backend.models.entity.UserPlant;
import com.plantastic.backend.security.user.CustomUserDetails;
import com.plantastic.backend.service.UserPlantService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user-plants")
@RequiredArgsConstructor
public class UserPlantController {

    private final UserPlantService userPlantService;

  @PostMapping(value = "/create-one", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createOneUserPlant(
            @RequestPart("data") CreateUserPlantRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser //Automatic injection of the current user by Spring
    ) throws IOException {
        UserPlant userPlant = userPlantService.createOneUserPlant(request, file, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("UserPlant successfully created: " + userPlant.getId());
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<UserPlantDetailsDto> getUserPlantDetailsById(@PathVariable("id") Long userPlantId) {
        UserPlantDetailsDto userPlantDetailsDto = userPlantService.getUserPlantDetailsById(userPlantId);
        return ResponseEntity.ok(userPlantDetailsDto);
    }

    @PatchMapping("/water-one/{id}")
    public ResponseEntity<UserPlantDetailsDto> waterUserPlantbyId(
            @PathVariable("id") Long userPlantId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate wateringDate) {
        UserPlantDetailsDto userPlantDetailsDto = userPlantService.updateWateringDaysForOneUserPlant(userPlantId, wateringDate);
        return ResponseEntity.ok(userPlantDetailsDto);
    }

    @PatchMapping("/water-multiples")
    public ResponseEntity<List<UserPlantSummaryDto>> waterMultiplesUserPlantByIds(
            @RequestBody List<Long> userPlantIds,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate wateringDate) {
        List<UserPlantSummaryDto> summaries = userPlantService.updateWateringDaysForMultiplesUserPlants(userPlantIds, wateringDate);
        return ResponseEntity.ok(summaries);
    }


  @DeleteMapping("/delete-one/{id}")
  public ResponseEntity<Map<String, String>> deleteOneUserPlant(@PathVariable("id") Long userPlantId) {
    log.debug("Deleting plant with id: {}", userPlantId);

    String message = "message";
    String success = "success";
    try {
      userPlantService.deleteUserPlantById(userPlantId);
      log.info("User plant (id: {}) has been successfully deleted", userPlantId);

      return ResponseEntity.ok(Map.of(
        message, "Plant deleted successfully",
        success, "true"
      ));
    } catch (EntityNotFoundException e) {
      log.warn("Plant with id {} deletion failed: {}", userPlantId, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of(message, e.getMessage(), success, "false"));
    } catch (Exception e) {
      log.error("Unexpected error deleting plant with id {}: ", userPlantId, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of(message, "An unexpected error occurred", success, "false"));
    }
  }
}
