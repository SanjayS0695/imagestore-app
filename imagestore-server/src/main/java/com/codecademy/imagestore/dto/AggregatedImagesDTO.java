package com.codecademy.imagestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class AggregatedImagesDTO {

    List<ImageDataDTO> images;

    public AggregatedImagesDTO() {
        images = new ArrayList<>();
    }
}
