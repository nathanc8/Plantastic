package com.plantastic.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantastic.backend.dto.json.PlantDtoFromJson;
import com.plantastic.backend.models.entity.Plant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class JsonService {

    private final RestTemplate restTemplate;

    public JsonService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<PlantDtoFromJson> readBackupPlantDbJson(String jsonFilePathFromResources) throws Exception {
        ClassPathResource resource = new ClassPathResource(jsonFilePathFromResources);

        try (InputStream inputStream = resource.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(inputStream, new TypeReference<List<PlantDtoFromJson>>() {
            });
        }
    }
}
