package com.plantastic.backend.service;

import com.plantastic.backend.dto.plants.CreateUserPlantRequest;
import com.plantastic.backend.dto.plants.UserPlantDetailsDto;
import com.plantastic.backend.dto.plants.UserPlantSummaryDto;
import com.plantastic.backend.mapper.UserPlantMapper;
import com.plantastic.backend.models.entity.Plant;
import com.plantastic.backend.models.entity.User;
import com.plantastic.backend.models.entity.UserPlant;
import com.plantastic.backend.repository.PlantRepository;
import com.plantastic.backend.repository.UserPlantRepository;
import com.plantastic.backend.repository.UserRepository;
import com.plantastic.backend.security.user.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPlantService {
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;
    private final UserPlantRepository userPlantRepository;
    private final UserPlantMapper userPlantMapper;
    private final CloudinaryService cloudinaryService;

    public UserPlant createOneUserPlant(CreateUserPlantRequest request, MultipartFile file, CustomUserDetails currentUser) throws IOException {
        //Get user
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", currentUser.getId());
                    return new EntityNotFoundException("User not found with id: " + currentUser.getId());
                });

        //Get plant
        Plant plant = plantRepository.findById(request.getPlantId())
                .orElseThrow(() -> {
                    log.error("Plant not found with id: {}", request.getPlantId());
                    return new EntityNotFoundException("Plant not found with id: " + request.getPlantId());
                });
        
      String userPlantImageUrl;
      if (file != null && !file.isEmpty()) {
        userPlantImageUrl = cloudinaryService.uploadFileAndGetUrl(file, "user plants");
      } else {
        userPlantImageUrl = plant.getImageUrl(); 
      }
      

        //Create and save userPlant
        UserPlant userPlant = new UserPlant(user, plant, request);
        userPlant.setImageUrl(userPlantImageUrl);
        return userPlantRepository.save(userPlant);
    }

    public UserPlantDetailsDto getUserPlantDetailsById(Long userPlantId) {
        return userPlantRepository.findUserPlantDetailsById(userPlantId);
    }

    //Update UserPlant's last watering & next watering
    public UserPlantDetailsDto updateWateringDaysForOneUserPlant(Long userPlantId, LocalDate date) {
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new EntityNotFoundException("UserPlant not found with id " + userPlantId));

       userPlant.setLastWatering(date);
       userPlant.setNextWatering();
       userPlantRepository.save(userPlant);

       return userPlantRepository.findUserPlantDetailsById(userPlantId);
    }

    public List<UserPlantSummaryDto> updateWateringDaysForMultiplesUserPlants(List<Long> userPlantsIds, LocalDate date) {
        List<UserPlantSummaryDto> summaries = new ArrayList<>();

        for (Long userPlantId : userPlantsIds) {
            UserPlantDetailsDto details = updateWateringDaysForOneUserPlant(userPlantId, date);

            //Conversion to summary
            UserPlantSummaryDto summary = userPlantMapper.toSummary(details);
            summaries.add(summary);
        }
        return summaries;
    }

    @Transactional
    public void deleteUserPlantById(Long userPlantId) {
      UserPlant userPlant = userPlantRepository.findById(userPlantId)
        .orElseThrow(() -> new EntityNotFoundException("UserPlant " + userPlantId + " not found"));
      userPlantRepository.delete(userPlant);
    }
}
