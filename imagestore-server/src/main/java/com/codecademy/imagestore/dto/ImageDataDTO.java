package com.codecademy.imagestore.dto;

import com.codecademy.imagestore.enums.ImageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDataDTO {

    private Long id;
    private String name;
    private String description;
    private ImageType type;
    private String filePath;
    private byte[] image;
}
