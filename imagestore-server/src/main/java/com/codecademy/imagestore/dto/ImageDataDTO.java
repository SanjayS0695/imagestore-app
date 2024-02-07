package com.codecademy.imagestore.dto;

import com.codecademy.imagestore.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDataDTO {

    private Long id;
    private String name;
    private String description;
    private ImageType type;
    private String filePath;
    private byte[] image;
}
