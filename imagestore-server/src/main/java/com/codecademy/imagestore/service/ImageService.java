package com.codecademy.imagestore.service;

import com.codecademy.imagestore.dto.AggregatedImagesDTO;
import com.codecademy.imagestore.dto.ImageDataDTO;
import com.codecademy.imagestore.entity.ImageData;
import com.codecademy.imagestore.enums.ImageType;
import com.codecademy.imagestore.exception.GenericAPIException;
import com.codecademy.imagestore.mapper.ImageDataMapper;
import com.codecademy.imagestore.repository.ImageRepository;
import com.codecademy.imagestore.utils.ImageStoreUtil;
import com.codecademy.imagestore.validator.ImageStoreValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Value("${image-store.filepath}")
    private String basePath;

    @Value("${image-store.maximum-file-size:0}")
    private String maximumImageSize;

    /**
     * Method to upload Image to the system
     *
     * @param imageFile
     * @return
     */
    @Transactional
    public Long uploadImage(MultipartFile imageFile, String description) {
        ImageStoreValidator.validateImage(imageFile);
        validateImageSize(imageFile.getSize());

        var fileName = ImageStoreUtil.getFilenameWithoutExtension(imageFile.getOriginalFilename());
        var completePath = ImageStoreUtil.generateFilePath(basePath, imageFile.getOriginalFilename());
        var imageData = new ImageData();
        imageData.setFilePath(completePath);
        imageData.setName(fileName);
        if (description != null && !description.isBlank()) {
            imageData.setDescription(description);
        }
        imageData.setType(ImageType.getImageType(imageFile.getContentType()));

        try {
            imageFile.transferTo(Path.of(completePath));
            log.info("[ImageStore - Upload Image] - Image was successfully saved to the file system.");
            try {
                var savedImageData = this.imageRepository.save(imageData);
                log.info("[ImageStore - Upload Image] - ImageData was successfully saved to the DB.");
                return savedImageData.getId();
            } catch (Exception e) {
                var image = new File(imageData.getFilePath());
                image.delete();
                log.error("[ImageStore - Upload Image] - Failed to upload image to the system.", e);
                throw new GenericAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "[ImageStore - Upload Image] - Image upload failed due to the error: " + e.getLocalizedMessage());
            }
        } catch (Exception e) {
            log.error("[ImageStore - Upload Image] - Failed to upload image to the system.", e);
            throw new GenericAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "[ImageStore - Upload Image] - Image upload failed due to the error: " + e.getLocalizedMessage());
        }
    }

    /**
     * Method to get the image from the system using image id
     *
     * @param id
     * @return
     */
    public ImageDataDTO getImageById(Long id) throws GenericAPIException {
        ImageStoreValidator.validateId(id);
        var optionalImage = this.imageRepository.findById(id);
        if (optionalImage.isPresent()) {
            try {
                var imageData =  optionalImage.get();
                var imageDTO = ImageDataMapper.INSTANCE.toDTO(imageData);
                var byteStream = Files.readAllBytes(new File(imageDTO.getFilePath()).toPath());
                imageDTO.setImage(byteStream);
                return imageDTO;
            } catch (Exception e) {
                log.error("[ImageStore - Get Image] - Failed to get the Image(Id: {}) data from the system.", id, e);
                throw new GenericAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to load Image from system due to error: " + e.getLocalizedMessage());
            }
        } else {
            log.error("[ImageStore - Get Image] - Image not found for the provided id({}).", id);
            throw new GenericAPIException(HttpStatus.NO_CONTENT, "[ImageStore - Get Image] - File not found for the provided Id.");
        }
    }

    /**
     * Method to load the image as byte stream from the system using image id
     *
     * @param id
     * @return
     */
    public byte[] loadImageById(Long id) throws GenericAPIException {
        ImageStoreValidator.validateId(id);
        var optionalImage = this.imageRepository.findById(id);
        if (optionalImage.isPresent()) {
            try {
                var imageData = optionalImage.get();
                return Files.readAllBytes(new File(imageData.getFilePath()).toPath());
            } catch (Exception e) {
                log.error("[ImageStore - Load Image] - Failed to load the Image(Id: {}) from the system.", id, e);
                throw new GenericAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to load image from system." + e.getLocalizedMessage());
            }
        } else {
            log.error("[ImageStore - Load Image] - Failed to load the Image(Id: {}) from the system.", id);
            throw new GenericAPIException(HttpStatus.NO_CONTENT, "[ImageStore - Delete Image] - File not found for the provided Id.");
        }
    }

    /**
     * Method to get all uploaded images from the system
     *
     * @return
     * @throws GenericAPIException
     */
    public AggregatedImagesDTO getAllImages() throws GenericAPIException {
        var imageDataList = this.imageRepository.findAll();
        AggregatedImagesDTO aggregatedImagesDTO = new AggregatedImagesDTO();
        if (!imageDataList.isEmpty()) {
            var imageDataDTOList = ImageDataMapper.INSTANCE.toDTOList(imageDataList);
            for (ImageDataDTO imageDataDTO: imageDataDTOList) {
                try {
                    var byteStream = Files.readAllBytes(new File(imageDataDTO.getFilePath()).toPath());
                    imageDataDTO.setImage(byteStream);
                } catch (IOException e) {
                    log.warn("[ImageStore - GetAllImages] - Failed to load the Image(Id: {}) from the system.",
                            imageDataDTO.getId());
                }
            }
            aggregatedImagesDTO.setImages(imageDataDTOList);
        } else {
            return aggregatedImagesDTO;
        }
        return aggregatedImagesDTO;
    }

    /**
     * Method to delete an image by Id
     *
     * @param id
     */
    @Transactional
    public void deleteImage(Long id) throws GenericAPIException {
        ImageStoreValidator.validateId(id);
        var fileData = this.imageRepository.findById(id);
        if (fileData.isPresent()) {
            var file = fileData.get();
            var image = new File(file.getFilePath());
            if (image.delete()) {
                this.imageRepository.deleteById(id);
                log.info("[ImageStore - Delete Image] - Image was successfully deleted from the file system.");
            } else {
                throw new GenericAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "[ImageStore - Delete Image] - Failed to delete the image from the server due to some error.");
            }
        } else {
            log.error("[ImageStore - Get Image] - Image not found for the provided id({}).", id);
            throw new GenericAPIException(HttpStatus.NO_CONTENT, "[ImageStore - Get Image] - File not found for the provided Id.");
        }
    }

    /**
     * Method to update Image and Desc for an already existing ImageData
     *
     * @param image
     * @param desc
     * @param id
     * @return
     * @throws GenericAPIException
     */
    @Transactional
    public Long updateImageById(MultipartFile image, String desc, Long id) throws GenericAPIException {
        if (null != image || desc != null) {
            ImageStoreValidator.validateId(id);
            var optionalOldImage = this.imageRepository.findById(id);
            if (optionalOldImage.isPresent()) {
                var existingImageData = optionalOldImage.get();
                if (null != image) {
                    updateExistingImageWithNewImage(id, existingImageData, image);
                }
                if (desc != null && !desc.isBlank()) {
                    existingImageData.setDescription(desc);
                }
                var savedImageData = this.imageRepository.save(existingImageData);
                log.info("[ImageStore - Update Image] - ImageData was successfully updated in the DB.");
                return savedImageData.getId();
            } else {
                log.error("[ImageStore - Update Image] - File not found for the given Id: {}.", id);
                throw new GenericAPIException(HttpStatus.NO_CONTENT, "File not found for the given Id: " + id);
            }
        } else {
            throw new GenericAPIException(HttpStatus.BAD_REQUEST, "Both Image and Desc cannot be null for the update.");
        }
    }

    /**
     * Method to validate the Image size
     *
     * @param imageSize
     */
    private void validateImageSize(Long imageSize) {
        if (imageSize > Long.parseLong(maximumImageSize)) {
            throw new GenericAPIException(HttpStatus.NOT_ACCEPTABLE,
                    String.format("The uploaded image is greater than the maximum allowed image size %s", maximumImageSize));
        }
    }

    /**
     * Method to update the existing image with the provided image.
     *
     * @param id
     * @param existingImageData
     * @param newImage
     */
    private void updateExistingImageWithNewImage(Long id, ImageData existingImageData, MultipartFile newImage) {
        ImageStoreValidator.validateImage(newImage);
        validateImageSize(newImage.getSize());
        try {
            var oldImage = new File(existingImageData.getFilePath());
            if (oldImage.delete()) {
                log.info("[ImageStore - Update Image] - Previous image was successfully deleted from the file system.");
                var newImagePath = ImageStoreUtil
                        .generateFilePath(basePath, newImage.getOriginalFilename());
                existingImageData.setFilePath(newImagePath);
                existingImageData.setName(ImageStoreUtil
                        .getFilenameWithoutExtension(newImage.getOriginalFilename()));
                newImage.transferTo(Path.of(newImagePath));
                log.info("[ImageStore - Update Image] - New image was successfully saved to the file system.");
            } else {
                log.error("[ImageStore - Update Image] - Failed to update the ImageData(Id: {}) with latest image as the previous file was not successfully deleted", id);
                throw new GenericAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete the previous image from the file system.");
            }
        } catch (IOException exception) {
            log.error("[ImageStore - Update Image] - Failed to update the ImageData(Id: {}) with latest image as the previous file was not successfully deleted", id, exception);
            throw new GenericAPIException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
        }
    }
}
