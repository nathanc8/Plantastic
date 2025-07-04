package com.plantastic.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CareGuideResponse {
    private List<CareGuideItem> data;
    // getter & setter
}
