package com.codecademy.imagestore.mapper;

import com.codecademy.imagestore.dto.ImageDataDTO;
import com.codecademy.imagestore.entity.ImageData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ImageDataMapper {
    ImageDataMapper INSTANCE = Mappers.getMapper(ImageDataMapper.class);

    ImageDataDTO toDTO(ImageData imageData);

    List<ImageDataDTO> toDTOList(List<ImageData> imageData);
}
